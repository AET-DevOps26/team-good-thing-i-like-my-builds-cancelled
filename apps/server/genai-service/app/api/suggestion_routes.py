import asyncio

from fastapi import APIRouter, WebSocket, WebSocketDisconnect

from app.services.suggestion_service import stream_suggestion

router = APIRouter(tags=["suggestion"])


@router.websocket("/api/v1/suggestion")
async def suggestion_websocket(websocket: WebSocket) -> None:
    await websocket.accept()
    current_task: asyncio.Task | None = None

    try:
        while True:
            data = await websocket.receive_json()
            msg_type = data.get("type")

            if msg_type == "text_update":
                if current_task and not current_task.done():
                    current_task.cancel()
                    try:
                        await current_task
                    except asyncio.CancelledError:
                        pass

                text_before = data.get("textBefore", "")
                text_after = data.get("textAfter", "")

                current_task = asyncio.create_task(
                    stream_suggestion(websocket, text_before, text_after)
                )

            elif msg_type == "cancel":
                if current_task and not current_task.done():
                    current_task.cancel()
                    try:
                        await current_task
                    except asyncio.CancelledError:
                        pass
                await websocket.send_json({"type": "done"})

    except WebSocketDisconnect:
        pass
    finally:
        if current_task and not current_task.done():
            current_task.cancel()
