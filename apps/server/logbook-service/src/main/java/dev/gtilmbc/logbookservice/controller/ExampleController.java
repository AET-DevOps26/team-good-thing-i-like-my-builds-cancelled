package dev.gtilmbc.logbookservice.controller;

import dev.gtilmbc.logbookservice.dto.ExampleCreateRequest;
import dev.gtilmbc.logbookservice.dto.ExampleDto;
import dev.gtilmbc.logbookservice.service.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examples")
@Tag(name = "Examples")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping
    @Operation(summary = "Get all examples")
    public List<ExampleDto> getExamples() {
        return exampleService.getAll();
    }

    @GetMapping("/{exampleId}")
    @Operation(summary = "Get an example by id")
    public ExampleDto getExample(@PathVariable Long exampleId) {
        return exampleService.get(exampleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new example")
    public ExampleDto save(@Valid @RequestBody ExampleCreateRequest request) {
        return exampleService.save(request);
    }
}
