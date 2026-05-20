from uvicorn import run

from app.core.config import get_app_host, get_app_port


if __name__ == "__main__":
    run("app.main:app", host=get_app_host(), port=get_app_port(), reload=True)
