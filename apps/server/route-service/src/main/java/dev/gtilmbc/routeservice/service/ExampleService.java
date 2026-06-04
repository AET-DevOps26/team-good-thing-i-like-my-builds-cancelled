package dev.gtilmbc.routeservice.service;

import dev.gtilmbc.routeservice.generated.model.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleService {

    public List<Station> getAll() {
        var station = new Station();
        station.setId("TestId");
        station.setTitle("Test Station");
        return List.of(station);
    }

}
