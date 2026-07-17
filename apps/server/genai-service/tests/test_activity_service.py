import asyncio
import json

import httpx
import pytest

from app.services import activity_service, lmstudio
from app.services.activity_service import (
    ActivitySuggestionError,
    _extract_json,
    _format_entry,
)


def test_extract_json_plain() -> None:
    data = _extract_json(
        '{"locations": [{"location": "Marburg", "activities": ["Schloss"]}]}'
    )
    assert data["locations"][0]["location"] == "Marburg"


def test_extract_json_with_markdown_fence() -> None:
    content = '```json\n{"locations": []}\n```'
    assert _extract_json(content) == {"locations": []}


def test_extract_json_with_surrounding_prose() -> None:
    content = 'Here you go:\n{"locations": []}\nEnjoy your trip!'
    assert _extract_json(content) == {"locations": []}


def test_extract_json_no_json() -> None:
    with pytest.raises(ActivitySuggestionError):
        _extract_json("Sorry, I cannot help with that.")


def test_extract_json_invalid_json() -> None:
    with pytest.raises(ActivitySuggestionError):
        _extract_json('{"locations": [unterminated')


def test_format_entry_compacts_description() -> None:
    entry = {
        "startCity": "Marburg",
        "destinationCity": "München Hbf",
        "endTime": "2026-07-01T12:30:00Z",
        "title": "Städtetrip",
        "description": "Erster   Tag:\n\nMarienplatz " + "x" * 500,
    }

    line = _format_entry(entry)

    assert line.startswith("- Marburg -> München Hbf (2026-07-01): Städtetrip — ")
    assert "\n" not in line
    assert len(line) < 400


def run_suggest(monkeypatch, related_entries: list[dict]) -> dict:
    """Run suggest_activities with mocked retrieval and capture the LLM payload."""
    captured: dict = {}

    def handler(request: httpx.Request) -> httpx.Response:
        captured["json"] = json.loads(request.content)
        return httpx.Response(
            200, json={"choices": [{"message": {"content": '{"locations": []}'}}]}
        )

    transport = httpx.MockTransport(handler)

    class _PatchedClient(httpx.AsyncClient):
        def __init__(self, **kwargs) -> None:
            kwargs["transport"] = transport
            super().__init__(**kwargs)

    monkeypatch.setattr(activity_service.httpx, "AsyncClient", _PatchedClient)
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "test-model")

    async def fake_fetch() -> list[dict]:
        return related_entries

    async def fake_related(locations: list[str], entries: list[dict]) -> list[dict]:
        assert locations == ["Frankfurt (Main) Hbf", "München Hbf"]
        return entries

    monkeypatch.setattr(activity_service, "fetch_entries", fake_fetch)
    monkeypatch.setattr(activity_service, "find_related_entries", fake_related)

    asyncio.run(
        activity_service.suggest_activities("München Hbf", ["Frankfurt (Main) Hbf"])
    )
    return captured["json"]


def test_prompt_includes_related_logbook_entries(monkeypatch) -> None:
    entry = {
        "startCity": "Marburg",
        "destinationCity": "München Hbf",
        "endTime": "2026-05-10T18:00:00Z",
        "title": "Museumstour",
        "description": "Deutsches Museum besucht.",
    }

    payload = run_suggest(monkeypatch, [entry])

    user_content = payload["messages"][1]["content"]
    assert "Past journeys from the user's logbook" in user_content
    assert "Museumstour" in user_content
    assert "Deutsches Museum besucht." in user_content


def test_prompt_has_no_logbook_block_without_related_entries(monkeypatch) -> None:
    payload = run_suggest(monkeypatch, [])

    user_content = payload["messages"][1]["content"]
    assert "Past journeys" not in user_content
    assert user_content.endswith("Interchange stations: Frankfurt (Main) Hbf")
