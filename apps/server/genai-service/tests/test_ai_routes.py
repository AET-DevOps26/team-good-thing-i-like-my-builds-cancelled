from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def test_generate_report() -> None:
    response = client.get("/api/v1/ai/generate-report", params={"description": "Visited Berlin"})
    assert response.status_code == 200
    assert "report" in response.json()


def test_recommend_destinations() -> None:
    response = client.get("/api/v1/ai/recommend-destinations", params={"visited": "Berlin,Hamburg"})
    assert response.status_code == 200
    body = response.json()
    assert "recommendations" in body
    assert len(body["recommendations"]) == 2
