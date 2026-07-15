import pytest
from fastapi.testclient import TestClient

from src.generated.models.activity_suggestion_response import ActivitySuggestionResponse
from src.generated.models.location_suggestion import LocationSuggestion

import app.api.activity_routes as activity_routes
from app.main import app
from app.services.activity_service import ActivitySuggestionError

client = TestClient(app)


def test_suggest_activities(monkeypatch: pytest.MonkeyPatch) -> None:
    async def fake_suggest(destination: str, interchanges: list[str]) -> ActivitySuggestionResponse:
        assert destination == "München Hbf"
        assert interchanges == ["Frankfurt (Main) Hbf"]
        return ActivitySuggestionResponse(
            locations=[
                LocationSuggestion(location="Frankfurt (Main) Hbf", activities=["Skyline vom Main aus ansehen"]),
                LocationSuggestion(location="München Hbf", activities=["Marienplatz besuchen", "Englischer Garten"]),
            ]
        )

    monkeypatch.setattr(activity_routes, "suggest_activities", fake_suggest)

    response = client.post(
        "/api/v1/suggestion/activities",
        json={"destination": "München Hbf", "interchanges": ["Frankfurt (Main) Hbf"]},
    )
    assert response.status_code == 200
    body = response.json()
    assert len(body["locations"]) == 2
    assert body["locations"][1]["location"] == "München Hbf"
    assert body["locations"][1]["activities"] == ["Marienplatz besuchen", "Englischer Garten"]


def test_suggest_activities_without_interchanges(monkeypatch: pytest.MonkeyPatch) -> None:
    async def fake_suggest(destination: str, interchanges: list[str]) -> ActivitySuggestionResponse:
        assert interchanges == []
        return ActivitySuggestionResponse(
            locations=[LocationSuggestion(location=destination, activities=["Altstadt erkunden"])]
        )

    monkeypatch.setattr(activity_routes, "suggest_activities", fake_suggest)

    response = client.post("/api/v1/suggestion/activities", json={"destination": "Marburg"})
    assert response.status_code == 200
    assert response.json()["locations"][0]["location"] == "Marburg"


def test_suggest_activities_missing_destination() -> None:
    response = client.post("/api/v1/suggestion/activities", json={"interchanges": ["Kassel"]})
    assert response.status_code == 400
    assert "message" in response.json()


def test_suggest_activities_model_unavailable(monkeypatch: pytest.MonkeyPatch) -> None:
    async def fake_suggest(destination: str, interchanges: list[str]) -> ActivitySuggestionResponse:
        raise ActivitySuggestionError("GenAI model unavailable")

    monkeypatch.setattr(activity_routes, "suggest_activities", fake_suggest)

    response = client.post("/api/v1/suggestion/activities", json={"destination": "München Hbf"})
    assert response.status_code == 502
    assert response.json() == {"message": "GenAI model unavailable"}
