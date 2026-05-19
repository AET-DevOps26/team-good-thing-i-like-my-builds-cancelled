from fastapi import FastAPI
from uvicorn import run

app = FastAPI(title="GenAI Service", version="0.1.0")


@app.get("/api/v1/ai/generate-report")
def generate_report(description: str):
    """Generate a travel report from description."""
    enhanced_report = f"Enhanced Report: {description.upper()}"
    return {"report": enhanced_report}


@app.get("/api/v1/ai/recommend-destinations")
def recommend_destinations(visited: str):
    """Recommend destinations based on visited places."""
    visited_list = [v.strip() for v in visited.split(",") if v.strip()]
    recommendations = [f"Recommendation for {v}" for v in visited_list]
    return {"recommendations": recommendations}


@app.get("/health")
def health():
    """Health check."""
    return {"status": "ok"}


if __name__ == "__main__":
    run("main:app", host="0.0.0.0", port=9200, reload=True)
