package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.gtilmbc.routeservice.generated.model.TransportMode;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * LogbookEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogbookEntry {

  private UUID id;

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public LogbookEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogbookEntry(UUID id, String title, OffsetDateTime startTime, OffsetDateTime endTime, String startCity, String destinationCity, TransportMode transportMode, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.startTime = startTime;
    this.endTime = endTime;
    this.startCity = startCity;
    this.destinationCity = destinationCity;
    this.transportMode = transportMode;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public LogbookEntry id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  public LogbookEntry title(String title) {
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

  public LogbookEntry description(@Nullable String description) {
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

  public LogbookEntry startTime(OffsetDateTime startTime) {
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

  public LogbookEntry endTime(OffsetDateTime endTime) {
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

  public LogbookEntry startCity(String startCity) {
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

  public LogbookEntry destinationCity(String destinationCity) {
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

  public LogbookEntry startStationId(@Nullable String startStationId) {
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

  public LogbookEntry destinationStationId(@Nullable String destinationStationId) {
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

  public LogbookEntry transportMode(TransportMode transportMode) {
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

  public LogbookEntry createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LogbookEntry updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogbookEntry logbookEntry = (LogbookEntry) o;
    return Objects.equals(this.id, logbookEntry.id) &&
        Objects.equals(this.title, logbookEntry.title) &&
        Objects.equals(this.description, logbookEntry.description) &&
        Objects.equals(this.startTime, logbookEntry.startTime) &&
        Objects.equals(this.endTime, logbookEntry.endTime) &&
        Objects.equals(this.startCity, logbookEntry.startCity) &&
        Objects.equals(this.destinationCity, logbookEntry.destinationCity) &&
        Objects.equals(this.startStationId, logbookEntry.startStationId) &&
        Objects.equals(this.destinationStationId, logbookEntry.destinationStationId) &&
        Objects.equals(this.transportMode, logbookEntry.transportMode) &&
        Objects.equals(this.createdAt, logbookEntry.createdAt) &&
        Objects.equals(this.updatedAt, logbookEntry.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, startTime, endTime, startCity, destinationCity, startStationId, destinationStationId, transportMode, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogbookEntry {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    startCity: ").append(toIndentedString(startCity)).append("\n");
    sb.append("    destinationCity: ").append(toIndentedString(destinationCity)).append("\n");
    sb.append("    startStationId: ").append(toIndentedString(startStationId)).append("\n");
    sb.append("    destinationStationId: ").append(toIndentedString(destinationStationId)).append("\n");
    sb.append("    transportMode: ").append(toIndentedString(transportMode)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

