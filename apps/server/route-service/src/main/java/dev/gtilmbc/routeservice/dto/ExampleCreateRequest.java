package dev.gtilmbc.routeservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Create example request payload")
public record ExampleCreateRequest(
    @NotBlank
    @Schema(description = "Example name", example = "Route 66") String name
) {
}
