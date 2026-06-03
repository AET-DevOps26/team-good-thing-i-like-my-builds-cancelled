#!/bin/bash

# Generate Spring Stubs for API

set -euo pipefail

for path in route logbook; do

    echo "Generating code for $path..."

    openapi-generator-cli generate \
        -g spring \
        -i api/openapi.yaml \
        -o apps/server/${path}-service \
    	--additional-properties=basePackage=dev.gtilmbc.${path}service.generated,apiPackage=dev.gtilmbc.${path}service.generated.api,modelPackage=dev.gtilmbc.${path}service.generated.model,useTags=true,interfaceOnly=true

done
