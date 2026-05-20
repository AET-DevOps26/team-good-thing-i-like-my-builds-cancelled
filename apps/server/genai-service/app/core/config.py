import os


def get_app_host() -> str:
    return os.getenv("APP_HOST", "0.0.0.0")


def get_app_port() -> int:
    return int(os.getenv("APP_PORT", "9200"))
