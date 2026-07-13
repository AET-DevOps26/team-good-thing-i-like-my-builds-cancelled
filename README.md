# team-good-thing-i-like-my-builds-cancelled
Repository for team Good thing I like my builds Cancelled!

## Responsibilities

| **Name**   | **Systems** | **Feature**        |
|------------|-------------|--------------------|
| **Anian**  | Server      | Route Service      |
| **Jan**    | GenAI       | Suggestion Service |
| **Konrad** | Client      | Logbook Service    |

## Documentation

The full documentation can be found in the /docs folder with the following entries:

- [🟡 Architecture](docs/architecture.md)
- [🟡 Checklist](docs/checklist.md)
- [🔴 CI/CD](docs/ci-cd.md)
- [🔴 Client](docs/client.md)
- [🟢 Database](docs/database.md)
- [🔴 Deployment](docs/deployment.md)
- [🔴 GenAI](docs/genai.md)
- [🔴 Monitoring](docs/monitoring.md)
- [🟡 Problem Statement](docs/problem-statement.md)
- [🔴 Server](docs/server.md)
- [🔴 Testing](docs/testing.md)

## Deployed Live Demo

- [Kubernetes (Rancher)](https://cancelled.stud.k8s.aet.cit.tum.de/)
- [Azure](http://4.223.64.133:4567/plan)

## AI Microservice
The logbook suggestion service can be tested using the ansible deployment here: http://4.223.64.133:4567/. Navigate to the "Logbuch" page, type something, and watch the magic happen.

## Monitoring and Observability

Prometheus + Grafana is now integrated for all deployment modes:

- Local Docker Compose (`infra/docker/compose/local/docker-compose.yml`)
- Azure/Ansible deployment (`infra/ansible/templates/docker-compose.yml.j2` + `infra/ansible/playbooks/deploy.yml`)
- Kubernetes/Helm deployment (`infra/kubernetes/helm/templates/monitoring/monitoring-*.yml`)

### Collected Metrics

- `route-service` and `logbook-service`: Spring Boot Actuator Prometheus endpoint at `/actuator/prometheus`
- `genai-service`: FastAPI Prometheus endpoint at `/metrics`
- Core grading metrics are covered in dashboard/alerts:
	- request count
	- latency (p95)
	- error rate (5xx ratio)

### Alert Rules

Prometheus alert rules are defined in:

- `infra/monitoring/prometheus/rules/application-alerts.yml`

Implemented alerts:

- `HighErrorRate` (>5% 5xx for 10m)
- `HighLatencyP95` (p95 > 1s for 10m)
- `ServiceDown` (target unavailable for 2m)

### Dashboard Export

The exported dashboard JSON for submission is versioned in:

- `infra/monitoring/grafana/dashboards/backend-observability.json`

The same dashboard is also bundled in the Helm chart under:

- `infra/kubernetes/helm/files/grafana/backend-observability.json`

### Access

- Local/Ansible Prometheus: `http://<host>:9090`
- Local/Ansible Grafana: `http://<host>:3000` (default `admin/admin`)
- Kubernetes:
	- `kubectl -n monitoring port-forward svc/prometheus-service 9090:9090`
	- `kubectl -n monitoring port-forward svc/grafana-service 3000:3000`
