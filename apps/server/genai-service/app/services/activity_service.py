import json
import logging

import httpx

from src.generated.models.activity_suggestion_response import ActivitySuggestionResponse

from app.services.lmstudio import LMSTUDIO_BASE_URL, auth_headers, get_model

logger = logging.getLogger(__name__)

_SYSTEM_PROMPT = """\
You are a travel assistant for a train travel logbook. \
The user provides the destination station of a journey and optionally the \
interchange stations along the way. Suggest sights or activities at each of \
these locations.
Rules:
- Suggest 2 to 3 sights or activities per location.
- For interchange stations, prefer things that fit into a short stopover \
close to the station.
- Answer in German.
- Keep each suggestion to one short sentence.
- Reply with JSON only — no markdown, no explanations — using exactly this shape:
{"locations": [{"location": "<station name>", "activities": ["<suggestion>", "<suggestion>"]}]}
- Include one entry per provided location, in the given order, \
with the destination last.
"""


class ActivitySuggestionError(Exception):
    """Raised when no activity suggestion could be obtained from the model."""


def _extract_json(content: str) -> dict:
    """Parse the model reply into a dict, tolerating markdown fences and prose."""
    start = content.find("{")
    end = content.rfind("}")
    if start == -1 or end <= start:
        raise ActivitySuggestionError("Model reply contained no JSON object")
    try:
        return json.loads(content[start : end + 1])
    except json.JSONDecodeError as exc:
        raise ActivitySuggestionError("Model reply was not valid JSON") from exc


async def suggest_activities(
    destination: str,
    interchanges: list[str],
) -> ActivitySuggestionResponse:
    """Ask the local LMStudio model for sights/activities along the route."""
    user_content = f"Destination station: {destination}"
    if interchanges:
        user_content += "\nInterchange stations: " + ", ".join(interchanges)

    model = await get_model()
    payload = {
        "model": model,
        "stream": False,
        "messages": [
            {"role": "system", "content": _SYSTEM_PROMPT},
            {"role": "user", "content": user_content},
        ],
        "max_tokens": 1024,
        "temperature": 0.7,
    }

    logger.info("POST %s/v1/chat/completions (model=%s)", LMSTUDIO_BASE_URL, model)
    try:
        async with httpx.AsyncClient(timeout=httpx.Timeout(5.0, read=120.0)) as client:
            response = await client.post(
                f"{LMSTUDIO_BASE_URL}/v1/chat/completions",
                json=payload,
                headers=auth_headers(),
            )
            response.raise_for_status()
            message = response.json()["choices"][0]["message"]
            # Prefer actual content; fall back to reasoning_content
            # for thinking models that only use that field.
            content = message.get("content") or message.get("reasoning_content") or ""
    except httpx.HTTPError as exc:
        logger.error("LMStudio request failed: %s", exc)
        raise ActivitySuggestionError("GenAI model unavailable") from exc
    except (KeyError, IndexError, ValueError) as exc:
        logger.error("Unexpected LMStudio response shape: %s", exc)
        raise ActivitySuggestionError("Unexpected model response") from exc

    data = _extract_json(content)
    try:
        result = ActivitySuggestionResponse.from_dict(data)
    except Exception as exc:
        logger.error("Model JSON did not match expected schema: %s", exc)
        raise ActivitySuggestionError(
            "Model reply did not match expected schema"
        ) from exc

    logger.info("Activity suggestion complete (%d locations)", len(result.locations))
    return result
