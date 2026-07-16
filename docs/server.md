# Server

The server side of the project consists of 3 microservices, two of which are implemented in Java using SpringBoot. The third service is written in Python and contains the GenAI component. The documentation for the GenAI part can be found under ["GenAI"](genai.md). The SpringBoot services are described on this page. They can be found here: `/apps/server`.

## Testing

[![Test & Lint Server](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/test-server.yml/badge.svg?branch=main)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/test-server.yml)

### Unit Tests
Both the logbook and route service have unit tests to ensure the existing functionality stays intact when making changes. You can run the tests manually in each project using `./gradlew test`.

For each Pull Request that makes changes to either of the two services, the corresponding tests are run.

### Linting
We ensure good code quality by using `spotless` for code style and `spotbugs` to prevent bugs in the logbook and route services.

For each Pull Request that makes changes to either of the two services, the corresponding checks are run as part of the test action.

## Logbook Service
Logbook tests cover functionality regarding the creation (validity checking), finding, and deletion of logbook entries.

## Route Service
The route service tests cover basic functionality, like stations being returned correctly from the findStations endpoint, server failures being handled gracefully, and routing functionality finding routes.
