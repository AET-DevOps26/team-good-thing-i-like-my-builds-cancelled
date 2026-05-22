package dev.gtilmbc.routeservice.service;

import dev.gtilmbc.routeservice.dto.ExampleCreateRequest;
import dev.gtilmbc.routeservice.dto.ExampleDto;
import dev.gtilmbc.routeservice.model.Example;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleService {
    ArrayList<Example> examples = new ArrayList<>();

    public List<ExampleDto> getAll() {
        return examples.stream().map(this::toDto).toList();
    }

    public ExampleDto get(Long id) {
        return examples.stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Example not found"));
    }

    public ExampleDto save(ExampleCreateRequest request) {
        Example example = toEntity(request);

        // set id as random long
        example.setId(Math.round(Math.random() * 1000000));
        examples.add(toEntity(request));
        return get(example.getId());
    }

    private ExampleDto toDto(Example example) {
        return new ExampleDto(example.getId(), example.getName());
    }

    private Example toEntity(ExampleCreateRequest request) {
        Example example = new Example();
        example.setName(request.name());
        return example;
    }
}
