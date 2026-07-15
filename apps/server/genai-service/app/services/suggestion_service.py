import asyncio
import json
import logging

import httpx

from src.generated.models.suggestion_done import SuggestionDone
from src.generated.models.suggestion_token import SuggestionToken

from app.services.lmstudio import LMSTUDIO_BASE_URL, auth_headers, get_model

logger = logging.getLogger(__name__)

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

    model = await get_model()
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

    headers: dict[str, str] = {"Accept": "text/event-stream", **auth_headers()}

    logger.info("POST %s/v1/chat/completions (model=%s)", LMSTUDIO_BASE_URL, model)
    try:
        async with httpx.AsyncClient(timeout=httpx.Timeout(5.0, read=120.0)) as client:
            async with client.stream(
                "POST",
                f"{LMSTUDIO_BASE_URL}/v1/chat/completions",
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
                            token_msg = SuggestionToken(type="token", token=delta)
                            await websocket.send_json(token_msg.to_dict())
                    except (json.JSONDecodeError, KeyError, IndexError):
                        continue

        done_msg = SuggestionDone(type="done")
        await websocket.send_json(done_msg.to_dict())
        logger.info("Suggestion complete")
    except asyncio.CancelledError:
        raise
    except Exception as exc:
        logger.exception("Error streaming suggestion: %s", exc)
        await websocket.send_json(SuggestionDone(type="done").to_dict())
