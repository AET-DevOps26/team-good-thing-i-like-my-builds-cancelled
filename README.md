# team-good-thing-i-like-my-builds-cancelled
Repository for team Good thing I like my builds Cancelled!

## Responsibilities

| **Name**   | **Systems** | **Feature**        |
|------------|-------------|--------------------|
| **Anian**  | Server      | Route Service      |
| **Jan**    | GenAI       | Suggestion Service |
| **Konrad** | Client      | Logbook Service    |

## Deployed Live Demo

- [Kubernetes (Rancher)](https://cancelled.stud.k8s.aet.cit.tum.de/)
- [Azure](http://4.223.64.133:4567/plan)

## AI Microservice
The logbook suggestion service can be tested using the ansible deployment here: http://4.223.64.133:4567/. Navigate to the "Logbuch" page, type something, and watch the magic happen.

## TODO Lists

### 1) Graded Project Requirements Checklist (Project Details + Grading)
Use this list as the strict compliance checklist to verify you meet all graded requirements.

#### Team and Process Compliance
- [x] Team has 3 registered members with GitHub username, TUMonline login, and matriculation number documented.
- [x] Each member has a primary subsystem (client/server/GenAI) and contributes to cross-subsystem integration.
- [x] All work is done through feature branches and pull requests.
- [x] No normal direct commits to `main`.
- [x] Peer review and approval happen before each merge.
- [x] Contribution traceability is visible via commits, PR authorship, reviews, and infra work.
- [x] Planning/questions/feedback are tracked in official Artemis channels.

#### System Architecture and Stack Compliance
- [ ] Mono-repo contains client, server, GenAI, deployment, CI/CD, and docs.
- [x] Client is implemented with an allowed framework and communicates via REST.
- [ ] Server is Spring Boot and contains at least 3 distinct microservices.
- [ ] Database is persistent and schema is documented.
- [x] GenAI service is a separate Python microservice with defined interface.
- [ ] GenAI provides a real user-facing capability (not only technical presence).
- [ ] OpenAPI/Swagger documentation exists and is exposed.

#### Containerization and Local Reproducibility
- [ ] Each component has its own Dockerfile (client, server services, GenAI, DB).
- [x] Full system runs end-to-end via compose setup.
- [ ] Local startup is possible in 3 commands or fewer.
- [ ] Setup instructions are reproducible for a new user without hidden manual steps.

#### Kubernetes and Deployment Compliance
- [x] Deployment manifests/charts exist for Kubernetes.
- [x] CD deploys automatically to Kubernetes on merge to `main`.
- [x] Deployment works on course infra (Rancher) and Azure.
- [ ] Environment-specific config is externalized (env vars, secrets, config files).
- [ ] No hardcoded credentials/tokens/environment constants in source code.
- [x] A stable deployed URL is available for tutor interaction.

#### CI/CD Compliance
- [x] GitHub Actions pipeline runs on every PR.
- [x] CI builds all relevant services.
- [ ] CI runs automated tests.
- [ ] CI includes linting/static analysis where appropriate.
- [x] CI fails reliably on broken code/tests.

#### Observability Compliance
- [x] Prometheus is integrated for metrics collection.
- [x] Metrics include at least request count, latency, and error rate.
- [x] Grafana dashboards visualize meaningful system behavior.
- [x] Grafana dashboards are exported as `.json` files for submission.
- [x] At least one meaningful alert rule is configured and documented.

#### Testing Compliance
- [ ] Unit tests cover critical server logic.
- [ ] Unit tests cover relevant GenAI logic.
- [ ] Client tests cover key user workflows/interactions.
- [ ] All tests run automatically in CI.

#### Engineering Artifacts and Documentation Compliance
- [ ] High-level architecture description is available and matches implementation.
- [ ] Mandatory diagrams are present: Subsystem Decomposition, Use Case, Analysis Object Model.
- [ ] API documentation and Swagger/OpenAPI references are in README/docs.
- [ ] README documents setup, architecture, API docs, CI/CD, monitoring, and responsibilities.
- [x] Monitoring config, dashboards, and alerts are versioned in the repo.
- [ ] Testing instructions are documented.

#### Presentation and Oral Exam Readiness
- [ ] Team can demonstrate working end-to-end system live.
- [ ] Team can explain architecture, pipeline, trade-offs, and operational aspects.
- [ ] Each member can clearly explain and defend their own subsystem/artifact.
- [ ] Evidence of monthly progress/checkpoint improvements is prepared.

#### Failure Guard (Must Be True)
- [ ] Contributions are transparently documented (GitHub + Artemis evidence).
- [ ] Every member can explain their own subsystem during presentation/exam.
- [ ] End-to-end system works in a real demo.

### 2) Product/Feature Checklist (Problem Statement)
Use this as the functional roadmap for the Travel Journal product itself.

#### Trip Tracking
- [ ] Implement trip creation/edit/delete/list.
- [ ] Store when the trip happened and trip duration.
- [ ] Store destination data.
- [ ] Store transportation to/from destination.

#### AI-Assisted Reports
- [x] Provide AI-assisted writing for smooth trip descriptions.
- [ ] Integrate description generation directly in trip workflow.
- [ ] Ensure generated text can be reviewed/edited by the user.

#### AI Destination Recommendations
- [ ] Analyze user trip history for recommendation signals.
- [ ] Recommend new destinations based on user preferences/patterns.
- [ ] Support recommendation rationale (why this destination was suggested).

#### Travel Statistics
- [ ] Show number of trips.
- [ ] Show number of different states visited.
- [ ] Show total distance traveled.
- [ ] Show average trip length.
- [ ] Show average trip cost.
- [ ] Show most used transportation mode.
- [ ] Show delay-related statistics (delay frequency and average delay).

#### Train Journey Planning
- [ ] Provide fastest route option.
- [ ] Provide cheapest route option.
- [ ] Provide least-transfer route option.
- [ ] Provide routes with interesting transfer locations.
- [ ] Include reliability/delay likelihood in planning where possible.

#### User Experience and Scope Extensions
- [ ] Keep workflows usable for private travelers with varied experience.
- [ ] Ensure responsive UI for core workflows.
- [ ] (Optional) Add social sharing of trips.
- [ ] (Optional) Add achievements/badges (e.g., capitals visited, all states).

#### Scenario Coverage Checks
- [ ] Scenario: user logs a current trip and receives AI writing help.
- [ ] Scenario: frequent capital visitor receives smart next-capital recommendation.
- [ ] Scenario: user analyzes delays and chooses a more reliable route.

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
