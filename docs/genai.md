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

### Train Change Activity Suggestion (RAG)

The second functionality of the GenAI service is the suggestion of activities at train stops (with train changes) along a planned journey. The AI service sends 
the planned journey to an LLM, which then returns a list of activities at the train stops along the journey. RAG is used to enhance the suggestions of the LLM 
by providing it with the most relevant available logbook entries.
