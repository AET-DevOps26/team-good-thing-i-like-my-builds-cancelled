package dev.gtilmbc.routeservice.controller;

import dev.gtilmbc.routeservice.generated.api.RouteApi;
import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExampleController implements RouteApi {

    private final ExampleService exampleService;

    @Override
    public ResponseEntity<List<Station>> getStations(String q) {
        return ResponseEntity.ok(exampleService.getAll());
    }
}
