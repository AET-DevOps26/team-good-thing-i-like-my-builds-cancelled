import logging

from fastapi import FastAPI

from app.api.suggestion_routes import router as suggestion_router

logging.basicConfig(level=logging.INFO)

app = FastAPI(title="GenAI Service", version="0.1.0")

app.include_router(suggestion_router)


@app.get("/health")
def health() -> dict:
    """Health check."""
    return {"status": "ok"}
