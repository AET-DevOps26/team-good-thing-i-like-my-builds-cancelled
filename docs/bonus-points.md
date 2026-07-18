# Bonus Points

From the Bonus Points section, the following features have been implemented in the Travel Journal Application:

## Advanced AI Integration

The activity suggestion feature implements a complete RAG (retrieval-augmented generation) pipeline rather than a plain LLM call.
When a user opens a train connection, the GenAI service retrieves the user's past journeys from the logbook service, 
embeds them together with a query derived from the route (destination and interchange stations) using an embedding model served 
via the OpenAI-compatible /v1/embeddings API, and ranks them by cosine similarity in an in-memory vector store that caches embeddings 
per entry revision. The top-k most relevant journeys are then injected into the prompt as grounding context, so the model can personalize 
its sightseeing suggestions to the user's demonstrated interests and avoid recommending activities that were already logged at those locations.
