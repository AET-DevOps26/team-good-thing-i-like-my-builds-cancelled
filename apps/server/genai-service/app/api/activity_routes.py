import logging

from fastapi import APIRouter
from fastapi.responses import JSONResponse

from src.generated.models.activity_suggestion_request import ActivitySuggestionRequest
from src.generated.models.activity_suggestion_response import ActivitySuggestionResponse

from app.services.activity_service import ActivitySuggestionError, suggest_activities

logger = logging.getLogger(__name__)

router = APIRouter(tags=["suggestion"])


@router.post("/api/v1/suggestion/activities", response_model=ActivitySuggestionResponse)
async def post_activity_suggestions(request: ActivitySuggestionRequest):
    """Suggest sights/activities at the interchanges and the destination of a route."""
    try:
        return await suggest_activities(request.destination, request.interchanges or [])
    except ActivitySuggestionError as exc:
        return JSONResponse(status_code=502, content={"message": str(exc)})
