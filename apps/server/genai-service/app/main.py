import logging
import time
from fastapi import FastAPI, Request, Response
from prometheus_client import CONTENT_TYPE_LATEST, Counter, Histogram, generate_latest

from app.api.suggestion_routes import router as suggestion_router

logging.basicConfig(level=logging.INFO)

app = FastAPI(title="GenAI Service", version="0.1.0")

REQUEST_COUNT = Counter(
    "app_http_requests_total",
    "Total HTTP requests received by the GenAI service.",
    ["service", "method", "path", "status"],
)

REQUEST_ERROR_COUNT = Counter(
    "app_http_request_errors_total",
    "Total HTTP requests with 5xx status in the GenAI service.",
    ["service", "method", "path", "status"],
)

REQUEST_LATENCY = Histogram(
    "app_http_request_duration_seconds",
    "Request latency in seconds for the GenAI service.",
    ["service", "method", "path"],
    buckets=(0.05, 0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0),
)


@app.middleware("http")
async def record_metrics(request: Request, call_next):
    start = time.perf_counter()
    response = await call_next(request)
    duration = time.perf_counter() - start

    service = "genai-service"
    method = request.method
    path = request.url.path
    status = str(response.status_code)

    REQUEST_COUNT.labels(service=service, method=method, path=path, status=status).inc()
    REQUEST_LATENCY.labels(service=service, method=method, path=path).observe(duration)

    if response.status_code >= 500:
        REQUEST_ERROR_COUNT.labels(
            service=service, method=method, path=path, status=status
        ).inc()

    return response


app.include_router(suggestion_router)


@app.get("/metrics", include_in_schema=False)
def metrics() -> Response:
    return Response(generate_latest(), media_type=CONTENT_TYPE_LATEST)


@app.get("/health")
def health() -> dict:
    """Health check."""
    return {"status": "ok"}
