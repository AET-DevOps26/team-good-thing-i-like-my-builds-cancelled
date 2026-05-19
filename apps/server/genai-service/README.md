# GenAI Service (Minimal)

Minimal FastAPI-Service with two GET-Endpoints for Report-Generation and Recommendations.

## Quick Start

```bash
cd apps/server/genai-service

# Create venv (Python 3.12+ recommended)
python3 -m venv .venv

# Activate venv
source .venv/bin/activate

# Install dependencies
python -m pip install --upgrade pip
python -m pip install -r requirements.txt

# Run
python main.py
```

## IntelliJ Interpreter

- Settings/Preferences -> Project: <project name> -> Python Interpreter
- Add Interpreter -> Add Local Interpreter -> Existing
- Select: `apps/server/genai-service/.venv/bin/python`

### Troubleshooting

If `pip` is not found, always use `python -m pip` inside the venv.

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
