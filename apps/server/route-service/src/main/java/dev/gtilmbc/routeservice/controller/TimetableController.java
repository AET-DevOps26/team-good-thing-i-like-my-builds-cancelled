package dev.gtilmbc.routeservice.controller;

import dev.gtilmbc.routeservice.generated.api.RouteApi;
import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
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
}
