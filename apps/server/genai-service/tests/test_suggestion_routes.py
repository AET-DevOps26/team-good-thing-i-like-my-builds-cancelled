"""Unit tests for the /api/v1/suggestion websocket endpoint.

stream_suggestion is replaced by fakes so only the routing logic is
under test: task creation, cancellation of a running suggestion when a
new text_update or a cancel message arrives.
"""

import asyncio

from fastapi.testclient import TestClient

import app.api.suggestion_routes as suggestion_routes
from app.main import app

client = TestClient(app)


def text_update(text_before: str, text_after: str | None = None) -> dict:
    return {"type": "text_update", "textBefore": text_before, "textAfter": text_after}


def test_text_update_streams_tokens(monkeypatch) -> None:
    async def fake_stream(websocket, text_before: str, text_after: str) -> None:
        await websocket.send_json(
            {"type": "token", "token": f"echo:{text_before}|{text_after}"}
        )
        await websocket.send_json({"type": "done"})

    monkeypatch.setattr(suggestion_routes, "stream_suggestion", fake_stream)

    with client.websocket_connect("/api/v1/suggestion") as ws:
        ws.send_json(text_update("Meine Reise nach", "war schön."))
        assert ws.receive_json() == {
            "type": "token",
            "token": "echo:Meine Reise nach|war schön.",
        }
        assert ws.receive_json() == {"type": "done"}


def test_missing_text_after_defaults_to_empty_string(monkeypatch) -> None:
    received: dict = {}

    async def fake_stream(websocket, text_before: str, text_after: str) -> None:
        received["text_after"] = text_after
        await websocket.send_json({"type": "done"})

    monkeypatch.setattr(suggestion_routes, "stream_suggestion", fake_stream)

    with client.websocket_connect("/api/v1/suggestion") as ws:
        ws.send_json({"type": "text_update", "textBefore": "Nur davor"})
        assert ws.receive_json() == {"type": "done"}

    assert received["text_after"] == ""


def test_cancel_without_running_task_sends_done() -> None:
    with client.websocket_connect("/api/v1/suggestion") as ws:
        ws.send_json({"type": "cancel"})
        assert ws.receive_json() == {"type": "done"}


def test_new_text_update_cancels_running_stream(monkeypatch) -> None:
    cancelled: list[str] = []

    async def fake_stream(websocket, text_before: str, text_after: str) -> None:
        await websocket.send_json({"type": "token", "token": f"start:{text_before}"})
        try:
            await asyncio.Event().wait()  # stream until cancelled
        except asyncio.CancelledError:
            cancelled.append(text_before)
            raise

    monkeypatch.setattr(suggestion_routes, "stream_suggestion", fake_stream)

    with client.websocket_connect("/api/v1/suggestion") as ws:
        ws.send_json(text_update("first"))
        assert ws.receive_json() == {"type": "token", "token": "start:first"}

        ws.send_json(text_update("second"))
        assert ws.receive_json() == {"type": "token", "token": "start:second"}
        # "second" only starts after "first" was cancelled and awaited
        assert cancelled == ["first"]

        ws.send_json({"type": "cancel"})
        assert ws.receive_json() == {"type": "done"}
        assert cancelled == ["first", "second"]
