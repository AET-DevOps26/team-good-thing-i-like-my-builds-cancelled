package dev.gtilmbc.logbookservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.gtilmbc.logbookservice.generated.model.TransportMode;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateLogbookEntryRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:39.374777+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class UpdateLogbookEntryRequest {

  private String title;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private String startCity;

  private String destinationCity;

  private @Nullable String startStationId;

  private @Nullable String destinationStationId;

  private TransportMode transportMode;

  public UpdateLogbookEntryRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateLogbookEntryRequest(String title, OffsetDateTime startTime, OffsetDateTime endTime, String startCity, String destinationCity, TransportMode transportMode) {
    this.title = title;
    this.startTime = startTime;
    this.endTime = endTime;
    this.startCity = startCity;
    this.destinationCity = destinationCity;
    this.transportMode = transportMode;
  }

  public UpdateLogbookEntryRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull @Size(min = 1, max = 120) 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  public UpdateLogbookEntryRequest description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(max = 4000) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public UpdateLogbookEntryRequest startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   */
  @NotNull @Valid 
  @Schema(name = "startTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startTime")
  public OffsetDateTime getStartTime() {
    return startTime;
  }

  @JsonProperty("startTime")
  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public UpdateLogbookEntryRequest endTime(OffsetDateTime endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * Get endTime
   * @return endTime
   */
  @NotNull @Valid 
  @Schema(name = "endTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endTime")
  public OffsetDateTime getEndTime() {
    return endTime;
  }

  @JsonProperty("endTime")
  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public UpdateLogbookEntryRequest startCity(String startCity) {
    this.startCity = startCity;
    return this;
  }

  /**
   * Get startCity
   * @return startCity
   */
  @NotNull 
  @Schema(name = "startCity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startCity")
  public String getStartCity() {
    return startCity;
  }

  @JsonProperty("startCity")
  public void setStartCity(String startCity) {
    this.startCity = startCity;
  }

  public UpdateLogbookEntryRequest destinationCity(String destinationCity) {
    this.destinationCity = destinationCity;
    return this;
  }

  /**
   * Get destinationCity
   * @return destinationCity
   */
  @NotNull 
  @Schema(name = "destinationCity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destinationCity")
  public String getDestinationCity() {
    return destinationCity;
  }

  @JsonProperty("destinationCity")
  public void setDestinationCity(String destinationCity) {
    this.destinationCity = destinationCity;
  }

  public UpdateLogbookEntryRequest startStationId(@Nullable String startStationId) {
    this.startStationId = startStationId;
    return this;
  }

  /**
   * Optional station id from route service for train journeys.
   * @return startStationId
   */
  
  @Schema(name = "startStationId", description = "Optional station id from route service for train journeys.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startStationId")
  public @Nullable String getStartStationId() {
    return startStationId;
  }

  @JsonProperty("startStationId")
  public void setStartStationId(@Nullable String startStationId) {
    this.startStationId = startStationId;
  }

  public UpdateLogbookEntryRequest destinationStationId(@Nullable String destinationStationId) {
    this.destinationStationId = destinationStationId;
    return this;
  }

  /**
   * Optional station id from route service for train journeys.
   * @return destinationStationId
   */
  
  @Schema(name = "destinationStationId", description = "Optional station id from route service for train journeys.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("destinationStationId")
  public @Nullable String getDestinationStationId() {
    return destinationStationId;
  }

  @JsonProperty("destinationStationId")
  public void setDestinationStationId(@Nullable String destinationStationId) {
    this.destinationStationId = destinationStationId;
  }

  public UpdateLogbookEntryRequest transportMode(TransportMode transportMode) {
    this.transportMode = transportMode;
    return this;
  }

  /**
   * Get transportMode
   * @return transportMode
   */
  @NotNull @Valid 
  @Schema(name = "transportMode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("transportMode")
  public TransportMode getTransportMode() {
    return transportMode;
  }

  @JsonProperty("transportMode")
  public void setTransportMode(TransportMode transportMode) {
    this.transportMode = transportMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateLogbookEntryRequest updateLogbookEntryRequest = (UpdateLogbookEntryRequest) o;
    return Objects.equals(this.title, updateLogbookEntryRequest.title) &&
        Objects.equals(this.description, updateLogbookEntryRequest.description) &&
        Objects.equals(this.startTime, updateLogbookEntryRequest.startTime) &&
        Objects.equals(this.endTime, updateLogbookEntryRequest.endTime) &&
        Objects.equals(this.startCity, updateLogbookEntryRequest.startCity) &&
        Objects.equals(this.destinationCity, updateLogbookEntryRequest.destinationCity) &&
        Objects.equals(this.startStationId, updateLogbookEntryRequest.startStationId) &&
        Objects.equals(this.destinationStationId, updateLogbookEntryRequest.destinationStationId) &&
        Objects.equals(this.transportMode, updateLogbookEntryRequest.transportMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, startTime, endTime, startCity, destinationCity, startStationId, destinationStationId, transportMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateLogbookEntryRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    startCity: ").append(toIndentedString(startCity)).append("\n");
    sb.append("    destinationCity: ").append(toIndentedString(destinationCity)).append("\n");
    sb.append("    startStationId: ").append(toIndentedString(startStationId)).append("\n");
    sb.append("    destinationStationId: ").append(toIndentedString(destinationStationId)).append("\n");
    sb.append("    transportMode: ").append(toIndentedString(transportMode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

