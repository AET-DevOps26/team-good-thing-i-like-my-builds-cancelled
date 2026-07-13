
# TODO Lists

## 1) Graded Project Requirements Checklist (Project Details + Grading)
Use this list as the strict compliance checklist to verify you meet all graded requirements.

### Team and Process Compliance
- [x] Team has 3 registered members with GitHub username, TUMonline login, and matriculation number documented.
- [x] Each member has a primary subsystem (client/server/GenAI) and contributes to cross-subsystem integration.
- [x] All work is done through feature branches and pull requests.
- [x] No normal direct commits to `main`.
- [x] Peer review and approval happen before each merge.
- [x] Contribution traceability is visible via commits, PR authorship, reviews, and infra work.
- [x] Planning/questions/feedback are tracked in official Artemis channels.

### System Architecture and Stack Compliance
- [x] Mono-repo contains client, server, GenAI, deployment, CI/CD, and docs.
- [x] Client is implemented with an allowed framework and communicates via REST.
- [x] Server contains at least 3 distinct microservices.
- [x] Database is persistent and schema is documented.
- [x] GenAI service is a separate Python microservice with defined interface.
- [x] GenAI provides a real user-facing capability (not only technical presence).
- [ ] OpenAPI/Swagger documentation exists and is exposed.

### Containerization and Local Reproducibility
- [x] Each component has its own Dockerfile (client, server services, GenAI).
- [x] Full system runs end-to-end via compose setup.
- [ ] Local startup is possible in 3 commands or fewer.
- [ ] Setup instructions are reproducible for a new user without hidden manual steps.

### Kubernetes and Deployment Compliance
- [x] Deployment manifests/charts exist for Kubernetes.
- [x] CD deploys automatically to Kubernetes on merge to `main`.
- [x] Deployment works on course infra (Rancher) and Azure.
- [ ] Environment-specific config is externalized (env vars, secrets, config files).
- [ ] No hardcoded credentials/tokens/environment constants in source code.
- [x] A stable deployed URL is available for tutor interaction.

### CI/CD Compliance
- [x] GitHub Actions pipeline runs on every PR.
- [x] CI builds all relevant services.
- [ ] CI runs automated tests.
- [ ] CI includes linting/static analysis where appropriate.
- [x] CI fails reliably on broken code/tests.

### Observability Compliance
- [ ] Prometheus is integrated for metrics collection.
- [ ] Metrics include at least request count, latency, and error rate.
- [ ] Grafana dashboards visualize meaningful system behavior.
- [ ] Grafana dashboards are exported as `.json` files for submission.
- [ ] At least one meaningful alert rule is configured and documented.

### Testing Compliance
- [ ] Unit tests cover critical server logic.
- [ ] Unit tests cover relevant GenAI logic.
- [ ] Client tests cover key user workflows/interactions.
- [ ] All tests run automatically in CI.

### Engineering Artifacts and Documentation Compliance
- [ ] High-level architecture description is available and matches implementation.
- [ ] Mandatory diagrams are present: Subsystem Decomposition, Use Case, Analysis Object Model.
- [ ] API documentation and Swagger/OpenAPI references are in README/docs.
- [ ] README documents setup, architecture, API docs, CI/CD, monitoring, and responsibilities.
- [ ] Monitoring config, dashboards, and alerts are versioned in the repo.
- [ ] Testing instructions are documented.

## 2) Product/Feature Checklist (Problem Statement)
Use this as the functional roadmap for the Travel Journal product itself.

### Trip Tracking
- [ ] Implement trip creation/edit/delete/list.
- [ ] Store when the trip happened and trip duration.
- [ ] Store destination data.
- [ ] Store transportation to/from destination.

### AI-Assisted Reports
- [x] Provide AI-assisted writing for smooth trip descriptions.
- [ ] Integrate description generation directly in trip workflow.
- [ ] Ensure generated text can be reviewed/edited by the user.

### AI Destination Recommendations
- [ ] Analyze user trip history for recommendation signals.
- [ ] Recommend new destinations based on user preferences/patterns.
- [ ] Support recommendation rationale (why this destination was suggested).

### Travel Statistics
- [ ] Show number of trips.
- [ ] Show number of different states visited.
- [ ] Show total distance traveled.
- [ ] Show average trip length.
- [ ] Show average trip cost.
- [ ] Show most used transportation mode.
- [ ] Show delay-related statistics (delay frequency and average delay).

### Train Journey Planning
- [ ] Provide fastest route option.
- [ ] Provide cheapest route option.
- [ ] Provide least-transfer route option.
- [ ] Provide routes with interesting transfer locations.
- [ ] Include reliability/delay likelihood in planning where possible.

### User Experience and Scope Extensions
- [ ] Keep workflows usable for private travelers with varied experience.
- [ ] Ensure responsive UI for core workflows.
- [ ] (Optional) Add social sharing of trips.
- [ ] (Optional) Add achievements/badges (e.g., capitals visited, all states).

### Scenario Coverage Checks
- [ ] Scenario: user logs a current trip and receives AI writing help.
- [ ] Scenario: frequent capital visitor receives smart next-capital recommendation.
- [ ] Scenario: user analyzes delays and chooses a more reliable route.
