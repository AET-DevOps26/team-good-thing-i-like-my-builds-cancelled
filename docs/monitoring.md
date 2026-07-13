# Monitoring

The monitoring setup uses [Prometheus](https://prometheus.io/) for metrics collection and [Grafana](https://grafana.com/) for visualization.

All deployment modes have the monitoring stack available:

- Local Docker Compose (`infra/docker/compose/local/docker-compose.yml`)
- Azure/Ansible deployment (`infra/ansible/templates/docker-compose.yml.j2` + `infra/ansible/playbooks/deploy.yml`)
- Kubernetes/Helm deployment (`infra/kubernetes/helm/templates/monitoring/monitoring-*.yml`)

The deployed monitoring on K8s can be found here:
- [Grafana](https://cancelled.stud.k8s.aet.cit.tum.de/monitoring/grafana/)
- [Prometheus](https://cancelled.stud.k8s.aet.cit.tum.de/monitoring/prometheus/)

## Collected Metrics

- `route-service` and `logbook-service`: Spring Boot Actuator Prometheus endpoint at `/api/actuator/prometheus`
- `genai-service`: FastAPI Prometheus endpoint at `/api/metrics`
- the following metrics are covered in dashboards and alerts:
    - request count
    - latency (p95)
    - error rate (5xx ratio)

![](assets/monitoring-dashboard.png)

## Alert Rules

Prometheus alert rules are defined in:

- `infra/monitoring/prometheus/rules/application-alerts.yml`

Implemented alerts:

- `HighErrorRate` (>5% 5xx for 10m)
- `HighLatencyP95` (p95 > 1s for 10m)
- `ServiceDown` (target unavailable for 2m)

![](assets/monitoring-alerts.png)

## Dashboard Export

The exported dashboard JSON for submission is versioned in:

- `infra/monitoring/grafana/dashboards/backend-observability.json`

The same dashboard is also bundled in the Helm chart under:

- `infra/kubernetes/helm/files/grafana/backend-observability.json`

## Access

- Local/Ansible Prometheus: `http://<host>:9090`
- Local/Ansible Grafana: `http://<host>:3000` (default `admin/admin`)
- Kubernetes:
    - `kubectl -n cancelled-monitoring port-forward svc/prometheus-service 9090:9090`
    - `kubectl -n cancelled-monitoring port-forward svc/grafana-service 3000:3000`
