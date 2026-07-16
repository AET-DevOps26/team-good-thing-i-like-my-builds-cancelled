# GenAI

## Activity suggestions with RAG

`POST /api/v1/suggestion/activities` suggests sights/activities for the
destination and interchange stations of a route. Before calling the LLM, the
genai-service personalizes the prompt with the user's own travel history
(retrieval-augmented generation):

1. Logbook entries are fetched from the logbook-service
   (`LOGBOOK_BASE_URL`, default `http://logbook-service:8080/api`).
2. The entries are ranked by similarity to the requested route
   (`app/services/retrieval.py`):
   - If `LMSTUDIO_EMBEDDING_MODEL` is set, the LMStudio `/v1/embeddings`
     endpoint is used for semantic ranking (cosine similarity, with an
     in-process cache keyed by entry id + `updatedAt`).
   - Otherwise — or if the embeddings request fails — a keyword match on the
     city names is used as fallback.
3. The top 3 entries are added to the prompt so the model can match the
   user's interests and avoid suggesting things they have already done.

If the logbook-service is unreachable, the endpoint still works — just
without personalization.

## Testing
