import asyncio

import httpx

from app.services import logbook_client


def install_transport(monkeypatch, handler) -> None:
    transport = httpx.MockTransport(handler)

    class _PatchedClient(httpx.AsyncClient):
        def __init__(self, **kwargs) -> None:
            kwargs["transport"] = transport
            super().__init__(**kwargs)

    monkeypatch.setattr(logbook_client.httpx, "AsyncClient", _PatchedClient)


def test_fetches_all_pages(monkeypatch) -> None:
    def handler(request: httpx.Request) -> httpx.Response:
        page = int(request.url.params["page"])
        items = (
            [{"id": f"{page}-{i}"} for i in range(100)]
            if page == 0
            else [{"id": "1-0"}]
        )
        return httpx.Response(
            200, json={"items": items, "page": page, "size": 100, "totalElements": 101}
        )

    install_transport(monkeypatch, handler)

    entries = asyncio.run(logbook_client.fetch_entries())
    assert len(entries) == 101


def test_returns_empty_list_when_unavailable(monkeypatch) -> None:
    def handler(request: httpx.Request) -> httpx.Response:
        raise httpx.ConnectError("connection refused", request=request)

    install_transport(monkeypatch, handler)

    assert asyncio.run(logbook_client.fetch_entries()) == []


def test_stops_when_page_is_empty(monkeypatch) -> None:
    calls: list[int] = []

    def handler(request: httpx.Request) -> httpx.Response:
        page = int(request.url.params["page"])
        calls.append(page)
        return httpx.Response(
            200, json={"items": [], "page": page, "size": 100, "totalElements": 9999}
        )

    install_transport(monkeypatch, handler)

    assert asyncio.run(logbook_client.fetch_entries()) == []
    assert calls == [0]
