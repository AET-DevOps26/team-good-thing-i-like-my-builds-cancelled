"""Unit tests for app.services.suggestion_service.

The LMStudio backend is replaced by an httpx.MockTransport so the SSE
parsing, message building and error handling can be tested without a
running model server.
"""

import asyncio
import json

import httpx

from app.services import lmstudio, suggestion_service


class FakeWebSocket:
    """Records every JSON message the service sends."""

    def __init__(self) -> None:
        self.sent: list[dict] = []

    async def send_json(self, data: dict) -> None:
        self.sent.append(data)


def sse_body(chunks: list, include_done: bool = True) -> bytes:
    """Build an SSE response body from chat-completion chunks."""
    lines = []
    for chunk in chunks:
        payload = chunk if isinstance(chunk, str) else "data: " + json.dumps(chunk)
        lines.append(payload)
        lines.append("")
    if include_done:
        lines.append("data: [DONE]")
        lines.append("")
    return "\n".join(lines).encode()


def delta_chunk(delta: dict) -> dict:
    return {"choices": [{"delta": delta}]}


def install_transport(monkeypatch, handler) -> None:
    """Make the service's internal AsyncClient use a MockTransport."""
    transport = httpx.MockTransport(handler)

    class _PatchedClient(httpx.AsyncClient):
        def __init__(self, **kwargs) -> None:
            kwargs["transport"] = transport
            super().__init__(**kwargs)

    monkeypatch.setattr(suggestion_service.httpx, "AsyncClient", _PatchedClient)


def run_stream(
    monkeypatch, handler, text_before="Hello", text_after=""
) -> FakeWebSocket:
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "test-model")
    install_transport(monkeypatch, handler)
    websocket = FakeWebSocket()
    asyncio.run(
        suggestion_service.stream_suggestion(websocket, text_before, text_after)
    )
    return websocket


def test_streams_tokens_and_done(monkeypatch) -> None:
    body = sse_body(
        [
            delta_chunk({"content": "Der "}),
            delta_chunk({"content": "Strand"}),
        ]
    )

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(200, content=body)

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [
        {"type": "token", "token": "Der "},
        {"type": "token", "token": "Strand"},
        {"type": "done"},
    ]


def test_falls_back_to_reasoning_content(monkeypatch) -> None:
    body = sse_body([delta_chunk({"reasoning_content": "gedacht"})])

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(200, content=body)

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [
        {"type": "token", "token": "gedacht"},
        {"type": "done"},
    ]


def test_ignores_malformed_and_empty_chunks(monkeypatch) -> None:
    body = sse_body(
        [
            ": sse comment line",  # no "data: " prefix -> skipped
            "data: not-json",  # JSONDecodeError -> skipped
            {"unexpected": "shape"},  # KeyError -> skipped
            delta_chunk({}),  # empty delta -> no token
            delta_chunk({"content": "ok"}),
        ]
    )

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(200, content=body)

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [
        {"type": "token", "token": "ok"},
        {"type": "done"},
    ]


def test_stops_at_done_marker(monkeypatch) -> None:
    body = sse_body([delta_chunk({"content": "vor"})])
    body += sse_body([delta_chunk({"content": "nach [DONE]"})], include_done=False)

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(200, content=body)

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [
        {"type": "token", "token": "vor"},
        {"type": "done"},
    ]


def test_sends_done_on_http_error(monkeypatch) -> None:
    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(500, content=b"model exploded")

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [{"type": "done"}]


def test_sends_done_on_connection_error(monkeypatch) -> None:
    def handler(request: httpx.Request) -> httpx.Response:
        raise httpx.ConnectError("connection refused", request=request)

    websocket = run_stream(monkeypatch, handler)
    assert websocket.sent == [{"type": "done"}]


def test_request_payload_contains_context(monkeypatch) -> None:
    captured: dict = {}

    def handler(request: httpx.Request) -> httpx.Response:
        captured["json"] = json.loads(request.content)
        captured["headers"] = request.headers
        return httpx.Response(200, content=sse_body([]))

    run_stream(
        monkeypatch, handler, text_before="Wir fuhren nach", text_after="und dann heim."
    )

    payload = captured["json"]
    assert payload["model"] == "test-model"
    assert payload["stream"] is True
    assert payload["messages"][0]["role"] == "system"

    user_content = payload["messages"][1]["content"]
    assert "Text before cursor:\nWir fuhren nach" in user_content
    assert "Text after cursor:\nund dann heim." in user_content
    assert "authorization" not in captured["headers"]


def test_empty_text_after_is_omitted(monkeypatch) -> None:
    captured: dict = {}

    def handler(request: httpx.Request) -> httpx.Response:
        captured["json"] = json.loads(request.content)
        return httpx.Response(200, content=sse_body([]))

    run_stream(monkeypatch, handler, text_before="Nur davor", text_after="")

    user_content = captured["json"]["messages"][1]["content"]
    assert "Text after cursor" not in user_content


def test_api_key_sets_authorization_header(monkeypatch) -> None:
    captured: dict = {}

    def handler(request: httpx.Request) -> httpx.Response:
        captured["headers"] = request.headers
        return httpx.Response(200, content=sse_body([]))

    monkeypatch.setattr(lmstudio, "_API_KEY", "secret-key")
    run_stream(monkeypatch, handler)

    assert captured["headers"]["authorization"] == "Bearer secret-key"


def test_get_model_prefers_env_override(monkeypatch) -> None:
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "forced-model")
    assert asyncio.run(lmstudio.get_model()) == "forced-model"


def test_get_model_auto_detects_first_model(monkeypatch) -> None:
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "")

    def handler(request: httpx.Request) -> httpx.Response:
        assert request.url.path == "/v1/models"
        return httpx.Response(200, json={"data": [{"id": "gemma-3"}, {"id": "other"}]})

    install_transport(monkeypatch, handler)
    assert asyncio.run(lmstudio.get_model()) == "gemma-3"


def test_get_model_falls_back_on_empty_list(monkeypatch) -> None:
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "")

    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(200, json={"data": []})

    install_transport(monkeypatch, handler)
    assert asyncio.run(lmstudio.get_model()) == "local-model"


def test_get_model_falls_back_on_error(monkeypatch) -> None:
    monkeypatch.setattr(lmstudio, "_MODEL_OVERRIDE", "")

    def handler(request: httpx.Request) -> httpx.Response:
        raise httpx.ConnectError("connection refused", request=request)

    install_transport(monkeypatch, handler)
    assert asyncio.run(lmstudio.get_model()) == "local-model"
