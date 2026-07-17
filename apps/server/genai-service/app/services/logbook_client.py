import logging
import os

import httpx

logger = logging.getLogger(__name__)

LOGBOOK_BASE_URL = os.getenv("LOGBOOK_BASE_URL", "http://logbook-service:8080/api")

_PAGE_SIZE = 100
_MAX_PAGES = 3


async def fetch_entries() -> list[dict]:
    """Fetch logbook entries for personalization; empty list if unavailable."""
    entries: list[dict] = []
    try:
        async with httpx.AsyncClient(timeout=5.0) as client:
            for page in range(_MAX_PAGES):
                resp = await client.get(
                    f"{LOGBOOK_BASE_URL}/v1/logbook/entries",
                    params={"page": page, "size": _PAGE_SIZE},
                )
                resp.raise_for_status()
                body = resp.json()
                items = body.get("items", [])
                entries.extend(items)
                if not items or len(entries) >= body.get("totalElements", 0):
                    break
    except Exception as exc:
        logger.warning("Could not fetch logbook entries: %s", exc)
        return []

    logger.info("Fetched %d logbook entries", len(entries))
    return entries
