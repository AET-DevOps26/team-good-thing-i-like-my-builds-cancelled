package dev.gtilmbc.routeservice.controller;

import dev.gtilmbc.routeservice.generated.api.RouteApi;
import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.generated.model.TrainConnection;
import dev.gtilmbc.routeservice.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.OffsetDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200" })
public class TimetableController implements RouteApi {

    private final TimetableService timetableService;

    @Override
    public ResponseEntity<List<Station>> getStations(String q) {
        try {
            return ResponseEntity.ok(timetableService.findByName(q));
        } catch (Exception e) {
            e.printStackTrace(); // Error handling later -> Observability
            return ResponseEntity.ok(List.of());
        }
    }

    @Override
    public ResponseEntity<List<TrainConnection>> getConnections(OffsetDateTime time, String startStationId, String destinationStationId, List<String> viaStationIds) {
        try {
            // TODO: Via-Stations (follow up)
            return ResponseEntity.ok(timetableService.findConnections(startStationId, destinationStationId, time));
        } catch (Exception e) {
            e.printStackTrace(); // Error handling later -> Observability
            return ResponseEntity.ok(List.of());
        }
    }
}
