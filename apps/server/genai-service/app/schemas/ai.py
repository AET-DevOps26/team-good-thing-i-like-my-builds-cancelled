from pydantic import BaseModel
from typing import List


class GenerateReportResponse(BaseModel):
    report: str


class RecommendDestinationsResponse(BaseModel):
    recommendations: List[str]
