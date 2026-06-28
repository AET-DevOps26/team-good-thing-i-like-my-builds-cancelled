import logging

from fastapi import FastAPI

logging.basicConfig(level=logging.INFO)

from app.api.ai_routes import router as ai_router
from app.api.suggestion_routes import router as suggestion_router

app = FastAPI(title="GenAI Service", version="0.1.0")

app.include_router(ai_router)
app.include_router(suggestion_router)


@app.get("/health")
def health() -> dict:
    """Health check."""
    return {"status": "ok"}
