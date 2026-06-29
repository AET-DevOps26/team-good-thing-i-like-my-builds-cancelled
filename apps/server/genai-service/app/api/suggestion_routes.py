import asyncio

from fastapi import APIRouter, WebSocket, WebSocketDisconnect

from src.generated.models.cancel_suggestion import CancelSuggestion
from src.generated.models.suggestion_done import SuggestionDone
from src.generated.models.text_update import TextUpdate

from app.services.suggestion_service import stream_suggestion

router = APIRouter(tags=["suggestion"])


async def _cancel(task: asyncio.Task) -> None:
    task.cancel()
    try:
        await task
    except (asyncio.CancelledError, Exception):
        pass


@router.websocket("/api/v1/suggestion")
async def suggestion_websocket(websocket: WebSocket) -> None:
    await websocket.accept()
    current_task: asyncio.Task | None = None

    try:
        while True:
            data = await websocket.receive_json()
            msg_type = data.get("type")

            if msg_type == "text_update":
                msg = TextUpdate.from_dict(data)

                if current_task and not current_task.done():
                    await _cancel(current_task)

                current_task = asyncio.create_task(
                    stream_suggestion(websocket, msg.text_before, msg.text_after or "")
                )

            elif msg_type == "cancel":
                CancelSuggestion.from_dict(data)  # validate schema

                if current_task and not current_task.done():
                    await _cancel(current_task)
                    current_task = None
                try:
                    done = SuggestionDone(type="done")
                    await websocket.send_json(done.to_dict())
                except Exception:
                    pass

    except WebSocketDisconnect:
        pass
    finally:
        if current_task and not current_task.done():
            await _cancel(current_task)
