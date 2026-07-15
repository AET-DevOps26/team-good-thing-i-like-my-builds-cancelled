#!/bin/bash

# Generate Python FastAPI stubs for the genAI service

set -euo pipefail

echo "Generating code for genai-service..."

npx @openapitools/openapi-generator-cli generate \
    -g python-fastapi \
    --global-property apis=Ai,models=TextUpdate:CancelSuggestion:SuggestionToken:SuggestionDone:ActivitySuggestionRequest:ActivitySuggestionResponse:LocationSuggestion,supportingFiles=false \
    -i api/openapi.yaml \
    -o apps/server/genai-service \
    --additional-properties=packageName=generated,useTags=true

find apps/server/genai-service/src/generated -name '*.py' \
    -exec sed -i.bak 's/^from generated\./from src.generated./' {} +
find apps/server/genai-service/src/generated -name '*.py.bak' -delete
