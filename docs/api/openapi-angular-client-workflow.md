# OpenAPI Angular Client Workflow

This repository uses the Spring route service as the source of truth and generates the Angular client from its OpenAPI document.

## What generates what

- The Spring route service publishes OpenAPI at `http://localhost:9100/v3/api-docs`.
- `apps/client/package.json` fetches that document into `libs/contracts/openapi/openapi.json`.
- `apps/client/package.json` then runs OpenAPI Generator to produce the Angular client in `libs/contracts/openapi/src/lib/generated`.
- `libs/contracts/openapi/src/index.ts` re-exports the generated output so apps can import from `@contracts/openapi`.

## Where to find it

- OpenAPI source: `apps/server/route-service/src/main/java/dev/gtilmbc/routeservice/controller/ExampleController.java`
- Request/response DTOs: `apps/server/route-service/src/main/java/dev/gtilmbc/routeservice/dto/`
- Generator config: `apps/client/openapitools.json` and `apps/client/openapi-generator.config.json`
- Generated contract library: `libs/contracts/openapi/`
- Angular example UI: `apps/client/src/app/app.ts`, `apps/client/src/app/app.html`, `apps/client/src/app/app.scss`
- Angular example service: `apps/client/src/app/examples.service.ts`
- Local dev proxy: `apps/client/proxy.conf.json`

## How to use it

1. Start the Spring route service.
2. Run the fetch and generation commands from `apps/client`.
3. Import generated API services and models from `@contracts/openapi`.
4. Run the Angular app with the local proxy so `/api/*` calls go to `http://localhost:9100`.

## The 3 commands

- `npm run openapi:fetch` downloads `/v3/api-docs` and stores it in `libs/contracts/openapi/openapi.json`.
- `npm run openapi:generate` reads that spec and generates the Angular client under `libs/contracts/openapi/src/lib/generated`.
- `npm run openapi:sync` runs both steps in order.

## Notes

- The current Angular examples UI uses the route service through a small data-access service at `apps/client/src/app/examples.service.ts`.
- The UI loads and refreshes example records through the `/api/v1/examples` endpoint.
- The `openapi:sync` flow is the source of the typed client library; the UI can consume it by importing from `@contracts/openapi`.
