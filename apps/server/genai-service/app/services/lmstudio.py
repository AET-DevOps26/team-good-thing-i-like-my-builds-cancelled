import logging
import os

import httpx

logger = logging.getLogger(__name__)

LMSTUDIO_BASE_URL = os.getenv("LMSTUDIO_BASE_URL", "http://127.0.0.1:1234")
_MODEL_OVERRIDE = os.getenv("LMSTUDIO_MODEL", "")
_API_KEY = os.getenv("LMSTUDIO_API_KEY", "")


def auth_headers() -> dict[str, str]:
    """Return authorization headers for LMStudio, if an API key is configured."""
    if _API_KEY:
        return {"Authorization": f"Bearer {_API_KEY}"}
    return {}


async def get_model() -> str:
    """Return the model name — env override or first loaded model from LMStudio."""
    if _MODEL_OVERRIDE:
        logger.info("Using model override: %s", _MODEL_OVERRIDE)
        return _MODEL_OVERRIDE
    try:
        async with httpx.AsyncClient(timeout=5.0) as client:
            resp = await client.get(f"{LMSTUDIO_BASE_URL}/v1/models")
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
