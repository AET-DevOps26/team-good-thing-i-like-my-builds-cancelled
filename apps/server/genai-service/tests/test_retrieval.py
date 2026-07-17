"""Unit tests for app.services.retrieval.

The LMStudio embeddings endpoint is replaced by an httpx.MockTransport,
mirroring the approach in test_suggestion_service.py.
"""

import asyncio
import json

import httpx

from app.services import retrieval


def make_entry(
    entry_id: str, title: str, description: str = "", city: str = ""
) -> dict:
    return {
        "id": entry_id,
        "updatedAt": "2026-07-01T00:00:00Z",
        "title": title,
        "startCity": "Marburg",
        "destinationCity": city,
        "description": description,
        "endTime": "2026-07-01T12:00:00Z",
    }


def install_transport(monkeypatch, handler) -> None:
    transport = httpx.MockTransport(handler)

    class _PatchedClient(httpx.AsyncClient):
        def __init__(self, **kwargs) -> None:
            kwargs["transport"] = transport
            super().__init__(**kwargs)

    monkeypatch.setattr(retrieval.httpx, "AsyncClient", _PatchedClient)


def test_keyword_ranking_matches_city_names(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "")
    entries = [
        make_entry("1", "Wandern im Allgäu", city="Oberstdorf"),
        make_entry(
            "2", "Städtetrip", "Marienplatz und Museen in München", "München Hbf"
        ),
        make_entry("3", "Kurztrip nach Frankfurt", city="Frankfurt (Main) Hbf"),
    ]

    related = asyncio.run(
        retrieval.find_related_entries(["Frankfurt (Main) Hbf", "München Hbf"], entries)
    )

    ids = [entry["id"] for entry in related]
    assert set(ids) == {"2", "3"}
    assert "1" not in ids


def test_keyword_ranking_limits_results(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "")
    entries = [
        make_entry(str(i), f"Reise {i} nach München", city="München") for i in range(10)
    ]

    related = asyncio.run(retrieval.find_related_entries(["München Hbf"], entries, k=3))

    assert len(related) == 3


def test_empty_inputs_return_empty(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "")
    assert asyncio.run(retrieval.find_related_entries(["München"], [])) == []
    assert asyncio.run(retrieval.find_related_entries([], [make_entry("1", "x")])) == []


def test_embedding_ranking_orders_by_similarity(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "test-embedder")
    monkeypatch.setattr(retrieval, "_embedding_cache", {})

    vectors = {
        0: [1.0, 0.0],  # query
        1: [0.0, 1.0],  # entry a: orthogonal -> low similarity
        2: [1.0, 0.1],  # entry b: nearly parallel -> high similarity
    }

    def handler(request: httpx.Request) -> httpx.Response:
        payload = json.loads(request.content)
        assert payload["model"] == "test-embedder"
        data = [
            {"index": i, "embedding": vectors[i]} for i in range(len(payload["input"]))
        ]
        return httpx.Response(200, json={"data": data})

    install_transport(monkeypatch, handler)
    entries = [make_entry("a", "Reise A"), make_entry("b", "Reise B")]

    related = asyncio.run(retrieval.find_related_entries(["München"], entries, k=1))

    assert [entry["id"] for entry in related] == ["b"]


def test_embedding_cache_skips_known_entries(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "test-embedder")
    monkeypatch.setattr(retrieval, "_embedding_cache", {})
    input_sizes: list[int] = []

    def handler(request: httpx.Request) -> httpx.Response:
        payload = json.loads(request.content)
        input_sizes.append(len(payload["input"]))
        data = [
            {"index": i, "embedding": [1.0, float(i)]}
            for i in range(len(payload["input"]))
        ]
        return httpx.Response(200, json={"data": data})

    install_transport(monkeypatch, handler)
    entries = [make_entry("a", "Reise A"), make_entry("b", "Reise B")]

    asyncio.run(retrieval.find_related_entries(["München"], entries))
    asyncio.run(retrieval.find_related_entries(["München"], entries))

    # First call embeds query + 2 entries, second call only the query.
    assert input_sizes == [3, 1]


def test_falls_back_to_keywords_when_embeddings_fail(monkeypatch) -> None:
    monkeypatch.setattr(retrieval, "EMBEDDING_MODEL", "test-embedder")
    monkeypatch.setattr(retrieval, "_embedding_cache", {})

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(500, content=b"no embedding model loaded")

    install_transport(monkeypatch, handler)
    entries = [
        make_entry("1", "Wandern im Allgäu", city="Oberstdorf"),
        make_entry("2", "Städtetrip nach München", city="München Hbf"),
    ]

    related = asyncio.run(retrieval.find_related_entries(["München Hbf"], entries))

    assert [entry["id"] for entry in related] == ["2"]


def test_location_keywords_strip_station_noise() -> None:
    keywords = retrieval._location_keywords(
        ["Frankfurt (Main) Hbf", "Kassel-Wilhelmshöhe"]
    )
    assert "frankfurt" in keywords
    assert "kassel" in keywords
    assert "wilhelmshöhe" in keywords
    assert "hbf" not in keywords
    assert "main" not in keywords
