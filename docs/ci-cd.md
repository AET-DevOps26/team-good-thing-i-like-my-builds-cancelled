# CI/CD

The project contains a list of CI/CD pipelines that are implemented using GitHub Actions. All build workflows test and lint their subsystem, build the code, package the Dockerfile into an image an push the image to the GitHub Container Registry (ghcr). Deployment workflows run after merging to main and deploy the built images to the given environment. 

## [Build Server](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/server.yml)

[![Build Server](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/server.yml/badge.svg?branch=main)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/server.yml)

The server build workflow firstly checks for changes in one of the microservices. If changes are detected or the workflow has been triggered manually for a certain service, the action builds the Dockerfile of that service and pushes the image to the container registry.

![](assets/ci-server.png)

## [Build Client](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/client.yml)

[![Build Client](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/client.yml/badge.svg?branch=main)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/client.yml)

The client build action runs the following steps:
- install (installs npm packages if changed)
- test (runs workflow and component tests)
- lint (runs linter)
- build (builds the angular app)
- publish (packages the app into a docker image and pushes to ghcr)

![](assets/ci-client.png)

## [Build GenAI](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-service.yml)

[![Build GenAI](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-service.yml/badge.svg?branch=main)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-service.yml)

The GenAI build workflow builds the python app using the Dockerfile and pushes the image to the container registry.

## Run GenAI Tests

[![Run GenAI Tests](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-tests.yml/badge.svg)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-tests.yml)

This workflow runs testing and linting for the GenAI python service.

## Deployment


