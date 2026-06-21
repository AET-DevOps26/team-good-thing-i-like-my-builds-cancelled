package dev.gtilmbc.routeservice.model;

import dev.gtilmbc.routeservice.generated.model.Station;

import java.util.List;

public record VerbindungAbschnitt(String abfahrtsOrt, StationTimestamp abfahrt, String ankunftsOrt, StationTimestamp ankunft, List<Station> halte) {
}
