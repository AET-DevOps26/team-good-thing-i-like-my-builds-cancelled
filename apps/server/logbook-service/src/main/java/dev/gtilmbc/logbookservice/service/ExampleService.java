package dev.gtilmbc.logbookservice.service;

import dev.gtilmbc.logbookservice.dto.ExampleCreateRequest;
import dev.gtilmbc.logbookservice.dto.ExampleDto;
import dev.gtilmbc.logbookservice.model.Example;
import dev.gtilmbc.logbookservice.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public List<ExampleDto> getAll() {
        return exampleRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    public ExampleDto get(Long id) {
        return exampleRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Example not found"));
    }

    public ExampleDto save(ExampleCreateRequest request) {
        Example saved = exampleRepository.save(toEntity(request));
        return toDto(saved);
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
