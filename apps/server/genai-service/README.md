# GenAI Service (Minimal)

Minimal FastAPI-Service with two GET-Endpoints for Report-Generation and Recommendations.

## Quick Start

```bash
cd apps/server/genai-service

# Install dependencies
pip install -r requirements.txt

# Run
python main.py
```

Alternatively using Uvicorn:

```bash
uvicorn app.main:app --host 0.0.0.0 --port 9200 --reload
```

OpenAPI/Swagger:
- http://localhost:9200/docs
- http://localhost:9200/openapi.json

## Endpoints

### GET /api/v1/ai/generate-report?description=...

```bash
curl "http://localhost:9200/api/v1/ai/generate-report?description=Visited%20Berlin"
```

### GET /api/v1/ai/recommend-destinations?visited=...

```bash
curl "http://localhost:9200/api/v1/ai/recommend-destinations?visited=Berlin,Hamburg"
```
