# team-good-thing-i-like-my-builds-cancelled
Repository for team Good thing I like my builds Cancelled!

## Responsibilities

| **Name**   | **User**                                       | **Systems** | **Feature**        |
|------------|------------------------------------------------|-------------|--------------------|
| **Anian**  | [@anian03](https://github.com/anian03)         | Server      | Route Service      |
| **Jan**    | [@Jan-Thurner](https://github.com/Jan-Thurner) | GenAI       | Suggestion Service |
| **Konrad** | [@konrad2002](https://github.com/konrad2002)   | Client      | Logbook Service    |

> [!NOTE]
> Due to a not squashed merge at the beginning of the project and because of a lot of generated client code the contribution of all members might seem less equal than it is! In terms of working hours and commit-complexity all contributors participated equally in the project!

## Documentation

The full documentation can be found in the /docs folder with the following entries:

- [🟡 Architecture](docs/architecture.md)
- [🟡 Checklist](docs/checklist.md)
- [🟡 CI/CD](docs/ci-cd.md)
- [🟡 Client](docs/client.md)
- [🟢 Database](docs/database.md)
- [🔴 Deployment](docs/deployment.md)
- [🔴 GenAI](docs/genai.md)
- [🟢 Monitoring](docs/monitoring.md)
- [🟡 Problem Statement](docs/problem-statement.md)
- [🔴 Server](docs/server.md)
- [🟡 Testing](docs/testing.md)

For local setup see ["Deployment > Local"](docs/deployment.md#local)

## Deployed Live Demo

- [Kubernetes (Rancher)](https://cancelled.stud.k8s.aet.cit.tum.de/)
- [Azure](http://4.223.64.133:4567/plan)

## AI Microservice
The logbook suggestion service can be tested using the ansible deployment here: http://4.223.64.133:4567/. Navigate to the "Logbuch" page, type something, and watch the magic happen.
