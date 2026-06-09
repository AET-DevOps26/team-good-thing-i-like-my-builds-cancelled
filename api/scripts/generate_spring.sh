#!/bin/bash

# Generate Spring Stubs for API

set -euo pipefail

for path in route logbook; do

    echo "Generating code for $path..."
    
    tag="$(echo "$path" | awk '{print toupper(substr($0,1,1)) substr($0,2)}')"
    
    openapi-generator-cli generate \
        -g spring \
        --global-property apis=${tag},models,supportingFiles=ApiUtil.java \
        -i api/openapi.yaml \
        -o apps/server/${path}-service \
        --additional-properties=basePackage=dev.gtilmbc.${path}service.generated,apiPackage=dev.gtilmbc.${path}service.generated.api,modelPackage=dev.gtilmbc.${path}service.generated.model,useTags=true,interfaceOnly=true

done
