import logging

logging.basicConfig(level=logging.INFO)

from fastapi import FastAPI
from prometheus_fastapi_instrumentator import Instrumentator

from app.api.suggestion_routes import router as suggestion_router

app = FastAPI(title="GenAI Service", version="0.1.0")

Instrumentator().instrument(app).expose(app, include_in_schema=False)

app.include_router(suggestion_router)


@app.get("/health")
def health() -> dict:
    """Health check."""
    return {"status": "ok"}
