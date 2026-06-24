package dev.gtilmbc.routeservice.model;

import dev.gtilmbc.routeservice.generated.model.TrainConnection;
import dev.gtilmbc.routeservice.generated.model.TrainSegment;

import java.util.List;

public record Verbindung(List<VerbindungAbschnitt> verbindungsAbschnitte) {
    public TrainConnection asConnection() {
        TrainConnection connection = new TrainConnection();

        List<TrainSegment> segments = verbindungsAbschnitte.stream().map(VerbindungAbschnitt::asSegment).toList();
        connection.setSegments(segments);

        connection.setDepartureTime(segments.getFirst().getDepartureTime());
        connection.setArrivalTime(segments.getLast().getArrivalTime());

        return connection;
    }
}
