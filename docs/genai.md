# GenAI

The GenAI service is the third microservice of the project. It is written in Python and contains the GenAI component. Its documentation and detailed description 
can be found on this page. The GenAI service can be found here: `/apps/server/genai-service`.

## Testing

[![Run GenAI Tests](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-tests.yml/badge.svg)](https://github.com/AET-DevOps26/team-good-thing-i-like-my-builds-cancelled/actions/workflows/genai-tests.yml)

### Unit Tests

Both functionalities of the GenAI service have unit tests to ensure the existing functionality stays intact when making changes. You can run the tests manually
in the folder `apps/server/genai-service` using the command `.venv/bin/python -m pytest tests/ -v`.

If the dependencies are not installed, you can install them using these commands:

```bash
python -m venv .venv
.venv/bin/pip install -r requirements.txt
.venv/bin/python -m pytest tests/ -v
```

### Linting

Before running the tests, we ensure good code quality by using `ruff` for enforcing a consistent code style. The linting can also be run manually in the folder 
`apps/server/genai-service` using the commands `.venv/bin/ruff check .` and `.venv/bin/ruff format --check .`.

## Functionality


### Report Completion

The first functionality of the GenAI service is the suggestion of a continuation of a report. Using a websocket connection, updates to the report are sent to 
the GenAI service, which then feeds the reports current state, and the cursor position to an LLM. The LLM then returns a suggestion for the continuation of the report. 
The suggestion is then sent back to the client and can be accepted or rejected by the user.

### Activity suggestions with RAG

`POST /api/v1/suggestion/activities` suggests sights/activities for the
destination and interchange stations of a route. Before calling the LLM, the
GenAI-service personalizes the prompt with entries from the logbook
(retrieval-augmented generation):

1. Logbook entries are fetched from the logbook-service
   (`LOGBOOK_BASE_URL`, default `http://logbook-service:8080/api`).
2. The entries are ranked by similarity to the requested route
   (`app/services/retrieval.py`):
    - If `LMSTUDIO_EMBEDDING_MODEL` is set, the LMStudio `/v1/embeddings`
      endpoint is used for semantic ranking (cosine similarity, with an
      in-process cache keyed by entry id + `updatedAt`).
    - Otherwise, or if the embeddings request fails, a keyword match on the
      city names is used as fallback.
3. The top 3 entries are added to the prompt so the model can match the
   user's interests and avoid suggesting things they have already done.

If the logbook-service is unreachable, the endpoint still works — just
without personalization.
