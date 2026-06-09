#!/bin/bash

# Generate all APIs from the current spec

set -euo pipefail

$(dirname "$0")/generate_spring.sh
$(dirname "$0")/generate_typescript.sh
