from fastapi import FastAPI

from app.api.ai_routes import router as ai_router

app = FastAPI(title="GenAI Service", version="0.1.0")

app.include_router(ai_router)


@app.get("/health")
def health() -> dict:
    """Health check."""
    return {"status": "ok"}
