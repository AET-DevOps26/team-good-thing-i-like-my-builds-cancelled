#!/bin/bash

# Generate Python FastAPI stubs for the genAI service

set -euo pipefail

echo "Generating code for genai-service..."

npx @openapitools/openapi-generator-cli generate \
    -g python-fastapi \
    --global-property apis=Ai,models=TextUpdate:CancelSuggestion:SuggestionToken:SuggestionDone,supportingFiles=false \
    -i api/openapi.yaml \
    -o apps/server/genai-service/src/generated \
    --additional-properties=packageName=app.generated,useTags=true
