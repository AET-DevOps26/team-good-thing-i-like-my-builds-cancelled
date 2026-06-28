import asyncio
import json
import logging
import os

import httpx

logger = logging.getLogger(__name__)

_LMSTUDIO_BASE_URL = os.getenv("LMSTUDIO_BASE_URL", "http://127.0.0.1:1234")
_MODEL_OVERRIDE = os.getenv("LMSTUDIO_MODEL", "")
_API_KEY = os.getenv("LMSTUDIO_API_KEY", "")

_SYSTEM_PROMPT = """\
You are an inline autocomplete assistant for travel report writing. \
The user is writing a travel report and you must suggest a natural continuation \
of the text at the cursor position.
Rules:
- Continue the text naturally and seamlessly from the cursor position.
- Write in the same language and style as the existing text.
- Keep suggestions concise (1-3 sentences at most).
- Do NOT repeat text that is already written before the cursor.
- Do NOT add a title or introduction — just continue mid-text.
- If textAfter is provided, your continuation must flow naturally into it.
- Reply with the continuation text only, nothing else.
"""


async def _get_model() -> str:
    """Return the model name — env override or first loaded model from LMStudio."""
    if _MODEL_OVERRIDE:
        logger.info("Using model override: %s", _MODEL_OVERRIDE)
        return _MODEL_OVERRIDE
    try:
        async with httpx.AsyncClient(timeout=5.0) as client:
            resp = await client.get(f"{_LMSTUDIO_BASE_URL}/v1/models")
            resp.raise_for_status()
            models = resp.json().get("data", [])
            if models:
                model_id = models[0]["id"]
                logger.info("Auto-detected model: %s", model_id)
                return model_id
            logger.warning("LMStudio returned empty model list")
    except Exception as exc:
        logger.warning("Could not fetch model list: %s", exc)
    return "local-model"


async def stream_suggestion(
    websocket,
    text_before: str,
    text_after: str,
) -> None:
    """Stream tokens from a local LMStudio model to the websocket."""
    user_content = f"Text before cursor:\n{text_before}"
    if text_after:
        user_content += f"\n\nText after cursor:\n{text_after}"
    user_content += "\n\nPlease continue the text from the cursor position."

    model = await _get_model()
    payload = {
        "model": model,
        "stream": True,
        "messages": [
            {"role": "system", "content": _SYSTEM_PROMPT},
            {"role": "user", "content": user_content},
        ],
        "max_tokens": 256,
        "temperature": 0.7,
    }

    headers: dict[str, str] = {"Accept": "text/event-stream"}
    if _API_KEY:
        headers["Authorization"] = f"Bearer {_API_KEY}"

    logger.info("POST %s/v1/chat/completions (model=%s)", _LMSTUDIO_BASE_URL, model)
    try:
        async with httpx.AsyncClient(timeout=httpx.Timeout(5.0, read=120.0)) as client:
            async with client.stream(
                "POST",
                f"{_LMSTUDIO_BASE_URL}/v1/chat/completions",
                json=payload,
                headers=headers,
            ) as response:
                if response.status_code >= 400:
                    body = await response.aread()
                    logger.error(
                        "LMStudio %s: %s",
                        response.status_code,
                        body.decode(errors="replace"),
                    )
                    response.raise_for_status()
                async for line in response.aiter_lines():
                    if not line.startswith("data: "):
                        continue
                    data = line[6:].strip()
                    if data == "[DONE]":
                        break
                    try:
                        chunk = json.loads(data)
                        d = chunk["choices"][0]["delta"]
                        # Prefer actual content; fall back to reasoning_content
                        # for thinking models (Gemma 4) that only use that field.
                        delta = d.get("content") or d.get("reasoning_content") or ""
                        if delta:
                            await websocket.send_json({"type": "token", "token": delta})
                    except (json.JSONDecodeError, KeyError, IndexError):
                        continue

        await websocket.send_json({"type": "done"})
        logger.info("Suggestion complete")
    except asyncio.CancelledError:
        raise
    except Exception as exc:
        logger.exception("Error streaming suggestion: %s", exc)
        await websocket.send_json({"type": "done"})
