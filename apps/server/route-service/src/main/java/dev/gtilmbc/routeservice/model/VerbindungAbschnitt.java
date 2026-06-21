package dev.gtilmbc.routeservice.model;

import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.generated.model.TrainSegment;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public record VerbindungAbschnitt(String abfahrtsOrt, StationTimestamp abfahrt, String ankunftsOrt, StationTimestamp ankunft, List<Station> halte) {
    TrainSegment asSegment() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
        TrainSegment segment = new TrainSegment();

        String arrival = ankunft.sollzeit();
        String departure = abfahrt.sollzeit();

        OffsetDateTime arrivalTime = LocalDateTime
            .parse(arrival, dateFormatter)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toOffsetDateTime();
        OffsetDateTime departureTime = LocalDateTime
            .parse(departure, dateFormatter)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toOffsetDateTime();

        segment.setArrivalTime(arrivalTime);
        segment.setDepartureTime(departureTime);

        segment.setStops(halte);

        segment.setStart(halte.getFirst());
        segment.setEnd(halte.getLast());

        return segment;
    }
}
