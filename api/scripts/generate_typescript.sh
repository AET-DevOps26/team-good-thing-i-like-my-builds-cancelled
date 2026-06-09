#!/bin/bash

# Generate Angular Typescript API

set -euo pipefail

echo "Generating client code..."

npx @openapitools/openapi-generator-cli generate \
  -i ./api/openapi.yaml \
  -g typescript-angular \
  -o apps/client/src/generated
