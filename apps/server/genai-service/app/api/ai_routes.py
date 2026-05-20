from fastapi import APIRouter, Query

from app.schemas.ai import GenerateReportResponse, RecommendDestinationsResponse
from app.services.report_service import generate_report
from app.services.recommendation_service import recommend_destinations

router = APIRouter(prefix="/api/v1/ai", tags=["AI"])


@router.get("/generate-report", response_model=GenerateReportResponse)
def get_report(description: str = Query(..., min_length=1)) -> GenerateReportResponse:
    """Generate a travel report from a short description."""
    report = generate_report(description)
    return GenerateReportResponse(report=report)


@router.get("/recommend-destinations", response_model=RecommendDestinationsResponse)
def get_recommendations(visited: str = Query(..., min_length=1)) -> RecommendDestinationsResponse:
    """Recommend destinations based on a comma-separated list."""
    visited_list = [v.strip() for v in visited.split(",") if v.strip()]
    recommendations = recommend_destinations(visited_list)
    return RecommendDestinationsResponse(recommendations=recommendations)
