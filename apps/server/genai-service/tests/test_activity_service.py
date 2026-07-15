import pytest

from app.services.activity_service import ActivitySuggestionError, _extract_json


def test_extract_json_plain() -> None:
    data = _extract_json('{"locations": [{"location": "Marburg", "activities": ["Schloss"]}]}')
    assert data["locations"][0]["location"] == "Marburg"


def test_extract_json_with_markdown_fence() -> None:
    content = '```json\n{"locations": []}\n```'
    assert _extract_json(content) == {"locations": []}


def test_extract_json_with_surrounding_prose() -> None:
    content = 'Here you go:\n{"locations": []}\nEnjoy your trip!'
    assert _extract_json(content) == {"locations": []}


def test_extract_json_no_json() -> None:
    with pytest.raises(ActivitySuggestionError):
        _extract_json("Sorry, I cannot help with that.")


def test_extract_json_invalid_json() -> None:
    with pytest.raises(ActivitySuggestionError):
        _extract_json('{"locations": [unterminated')
