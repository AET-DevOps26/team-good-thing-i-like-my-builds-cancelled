#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$ROOT_DIR/infra/docker/compose/local/docker-compose.yml"
ENV_TEMPLATE_FILE="$ROOT_DIR/infra/docker/compose/local/.env.example"
ENV_FILE="$ROOT_DIR/infra/docker/compose/local/.env"

if [[ -t 1 ]]; then
    C_RESET="\033[0m"
    C_BOLD="\033[1m"
    C_BLUE="\033[34m"
    C_CYAN="\033[36m"
    C_GREEN="\033[32m"
    C_YELLOW="\033[33m"
    C_RED="\033[31m"
else
    C_RESET=""
    C_BOLD=""
    C_BLUE=""
    C_CYAN=""
    C_GREEN=""
    C_YELLOW=""
    C_RED=""
fi

ICON_INFO="ℹ"
ICON_OK="✔"
ICON_WARN="⚠"
ICON_ERR="✖"
ICON_STEP="▶"

info() {
    echo -e "${C_BLUE}${ICON_INFO}${C_RESET} $*"
}

ok() {
    echo -e "${C_GREEN}${ICON_OK}${C_RESET} $*"
}

warn() {
    echo -e "${C_YELLOW}${ICON_WARN}${C_RESET} $*"
}

err() {
    echo -e "${C_RED}${ICON_ERR}${C_RESET} $*"
}

step() {
    echo -e "${C_CYAN}${ICON_STEP}${C_RESET} $*"
}

read_key_from_env_file() {
    local file="$1"

    if [[ ! -f "$file" ]]; then
        echo ""
        return 0
    fi

    awk -F '=' '/^LMSTUDIO_API_KEY=/{sub(/^LMSTUDIO_API_KEY=/, ""); print; exit}' "$file"
}

write_key_to_env_file() {
    local file="$1"
    local key="$2"
    local tmp

    tmp="$(mktemp)"

    if [[ -f "$file" ]]; then
        awk -v key="$key" '
            BEGIN { written = 0 }
            /^LMSTUDIO_API_KEY=/ {
                print "LMSTUDIO_API_KEY=" key
                written = 1
                next
            }
            { print }
            END {
                if (!written) {
                    print "LMSTUDIO_API_KEY=" key
                }
            }
        ' "$file" > "$tmp"
    else
        printf 'LMSTUDIO_API_KEY=%s\n' "$key" > "$tmp"
    fi

    mv "$tmp" "$file"
}

show_header() {
    echo -e "${C_BOLD}${C_CYAN}╔══════════════════════════════════════════════════════════════╗${C_RESET}"
    echo -e "${C_BOLD}${C_CYAN}║${C_RESET}  ${C_BOLD}GTILMBC Local Stack Bootstrap${C_RESET}                               ${C_BOLD}${C_CYAN}║${C_RESET}"
    echo -e "${C_BOLD}${C_CYAN}╚══════════════════════════════════════════════════════════════╝${C_RESET}"
}

require_command() {
    local cmd="$1"
    local label="$2"
    if ! command -v "$cmd" >/dev/null 2>&1; then
        err "$label is required but was not found in PATH."
        exit 1
    fi
}

show_header

step "Checking prerequisites"
require_command docker "Docker"

if ! docker info >/dev/null 2>&1; then
    err "Docker daemon is not running. Start Docker and retry."
    exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
    err "Docker Compose (docker compose) is unavailable."
    exit 1
fi
ok "Docker and Compose are ready"

if [[ ! -f "$COMPOSE_FILE" ]]; then
    err "Compose file not found: $COMPOSE_FILE"
    exit 1
fi

if [[ ! -f "$ENV_FILE" ]]; then
    if [[ -f "$ENV_TEMPLATE_FILE" ]]; then
        info "Creating local env file from template"
        cp "$ENV_TEMPLATE_FILE" "$ENV_FILE"
    else
        warn "Template file missing. Creating env file from scratch"
        : > "$ENV_FILE"
    fi
fi

step "Resolving LMSTUDIO_API_KEY"
SECRET_VALUE="${LMSTUDIO_API_KEY:-}"
SECRET_SOURCE="environment"

if [[ -z "$SECRET_VALUE" ]]; then
    SECRET_VALUE="$(read_key_from_env_file "$ENV_FILE")"
    SECRET_SOURCE="$ENV_FILE"
fi

if [[ -z "$SECRET_VALUE" ]]; then
    echo
    info "LMSTUDIO_API_KEY is required for genai-service."
    printf "%b" "${C_BOLD}Paste LMSTUDIO_API_KEY:${C_RESET} "
    read -r -s SECRET_VALUE
    echo

    if [[ -z "$SECRET_VALUE" ]]; then
        err "No LMSTUDIO_API_KEY provided. Aborting."
        exit 1
    fi

    SECRET_SOURCE="interactive prompt"
fi

write_key_to_env_file "$ENV_FILE" "$SECRET_VALUE"
ok "LMSTUDIO_API_KEY stored in $ENV_FILE (source: $SECRET_SOURCE)"

step "Building Docker images"
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" build
ok "Image build completed"

step "Starting containers"
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" up -d --remove-orphans
ok "Containers are up"

echo
echo -e "${C_BOLD}Local environment is ready.${C_RESET}"
echo "Client:      http://localhost:4567"
echo "Docs portal: http://localhost:4570"
echo "Prometheus:  http://localhost:9090"
echo "Grafana:     http://localhost:3000"
echo "pgAdmin:     http://localhost:5050"
