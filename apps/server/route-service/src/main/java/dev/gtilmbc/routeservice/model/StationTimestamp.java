package dev.gtilmbc.routeservice.model;

import jakarta.annotation.Nullable;

public record StationTimestamp(String sollzeit, @Nullable String echtzeit) {
}
