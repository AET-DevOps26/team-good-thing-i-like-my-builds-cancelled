"""Semantic retrieval over logbook entries for personalized suggestions.

Ranks the user's past logbook entries by similarity to the current route.
Uses the LMStudio embeddings endpoint when LMSTUDIO_EMBEDDING_MODEL is set,
and falls back to keyword matching on the location names otherwise (or when
the embeddings request fails).
"""

import logging
import math
import os
import re

import httpx

from app.services.lmstudio import LMSTUDIO_BASE_URL, auth_headers

logger = logging.getLogger(__name__)

EMBEDDING_MODEL = os.getenv("LMSTUDIO_EMBEDDING_MODEL", "")

_DESCRIPTION_EXCERPT_CHARS = 500

# Embeddings keyed by "<entry id>:<updatedAt>" so edited entries are re-embedded.
_embedding_cache: dict[str, list[float]] = {}


def entry_text(entry: dict) -> str:
    """Compact text representation of a logbook entry for embedding/matching."""
    parts = [
        entry.get("title") or "",
        f"{entry.get('startCity') or ''} -> {entry.get('destinationCity') or ''}",
        (entry.get("description") or "")[:_DESCRIPTION_EXCERPT_CHARS],
    ]
    return "\n".join(part for part in parts if part.strip())


async def _embed(texts: list[str]) -> list[list[float]]:
    async with httpx.AsyncClient(timeout=httpx.Timeout(5.0, read=60.0)) as client:
        resp = await client.post(
            f"{LMSTUDIO_BASE_URL}/v1/embeddings",
            json={"model": EMBEDDING_MODEL, "input": texts},
            headers=auth_headers(),
        )
        resp.raise_for_status()
        data = sorted(resp.json()["data"], key=lambda item: item["index"])
        return [item["embedding"] for item in data]


def _cosine(a: list[float], b: list[float]) -> float:
    dot = sum(x * y for x, y in zip(a, b))
    norm = math.sqrt(sum(x * x for x in a)) * math.sqrt(sum(y * y for y in b))
    return dot / norm if norm else 0.0


def _location_keywords(locations: list[str]) -> list[str]:
    """Reduce station names to city keywords, e.g. 'Frankfurt (Main) Hbf' -> 'frankfurt'."""
    keywords = []
    for location in locations:
        cleaned = re.sub(r"\(.*?\)|\bhbf\b|\bbahnhof\b", " ", location.lower())
        keywords.extend(
            word for word in re.split(r"[^\wäöüß]+", cleaned) if len(word) >= 3
        )
    return keywords


def _keyword_ranking(locations: list[str], entries: list[dict], k: int) -> list[dict]:
    keywords = _location_keywords(locations)
    scored = []
    for entry in entries:
        text = entry_text(entry).lower()
        score = sum(text.count(keyword) for keyword in keywords)
        if score > 0:
            scored.append((score, entry))
    scored.sort(key=lambda pair: pair[0], reverse=True)
    return [entry for _, entry in scored[:k]]


async def _embedding_ranking(
    locations: list[str], entries: list[dict], k: int
) -> list[dict]:
    query = (
        "Zugreise nach " + ", ".join(locations) + ". "
        "Sehenswürdigkeiten und Aktivitäten an diesen Orten."
    )

    cache_keys = [f"{entry.get('id')}:{entry.get('updatedAt')}" for entry in entries]
    uncached = [i for i, key in enumerate(cache_keys) if key not in _embedding_cache]

    texts = [query] + [entry_text(entries[i]) for i in uncached]
    vectors = await _embed(texts)

    query_vector = vectors[0]
    for vector, i in zip(vectors[1:], uncached):
        _embedding_cache[cache_keys[i]] = vector

    scored = [
        (_cosine(query_vector, _embedding_cache[key]), entry)
        for key, entry in zip(cache_keys, entries)
    ]
    scored.sort(key=lambda pair: pair[0], reverse=True)
    return [entry for _, entry in scored[:k]]


async def find_related_entries(
    locations: list[str], entries: list[dict], k: int = 3
) -> list[dict]:
    """Return up to k logbook entries most related to the given route locations."""
    if not entries or not locations:
        return []

    if EMBEDDING_MODEL:
        try:
            related = await _embedding_ranking(locations, entries, k)
            logger.info("Retrieved %d related entries via embeddings", len(related))
            return related
        except Exception as exc:
            logger.warning("Embeddings unavailable, falling back to keywords: %s", exc)

    related = _keyword_ranking(locations, entries, k)
    logger.info("Retrieved %d related entries via keyword matching", len(related))
    return related
