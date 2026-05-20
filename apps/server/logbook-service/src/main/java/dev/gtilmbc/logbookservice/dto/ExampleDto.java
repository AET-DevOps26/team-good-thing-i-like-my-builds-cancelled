package dev.gtilmbc.logbookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Example response payload")
public record ExampleDto(
	@Schema(description = "Example identifier", example = "1") Long id,
	@Schema(description = "Example name", example = "Route 66") String name
) {
}
