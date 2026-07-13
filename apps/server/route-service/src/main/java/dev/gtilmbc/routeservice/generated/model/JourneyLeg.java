package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.gtilmbc.routeservice.generated.model.LocationRef;
import dev.gtilmbc.routeservice.generated.model.TransportMode;
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
 * JourneyLeg
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T10:57:03.123918+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class JourneyLeg {

  private TransportMode mode;

  private LocationRef start;

  private LocationRef end;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime departureTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime arrivalTime;

  private @Nullable String provider;

  private @Nullable String line;

  private @Nullable String notes;

  public JourneyLeg() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JourneyLeg(TransportMode mode, LocationRef start, LocationRef end, OffsetDateTime departureTime, OffsetDateTime arrivalTime) {
    this.mode = mode;
    this.start = start;
    this.end = end;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
  }

  public JourneyLeg mode(TransportMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull @Valid 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public TransportMode getMode() {
    return mode;
  }

  @JsonProperty("mode")
  public void setMode(TransportMode mode) {
    this.mode = mode;
  }

  public JourneyLeg start(LocationRef start) {
    this.start = start;
    return this;
  }

  /**
   * Get start
   * @return start
   */
  @NotNull @Valid 
  @Schema(name = "start", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("start")
  public LocationRef getStart() {
    return start;
  }

  @JsonProperty("start")
  public void setStart(LocationRef start) {
    this.start = start;
  }

  public JourneyLeg end(LocationRef end) {
    this.end = end;
    return this;
  }

  /**
   * Get end
   * @return end
   */
  @NotNull @Valid 
  @Schema(name = "end", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("end")
  public LocationRef getEnd() {
    return end;
  }

  @JsonProperty("end")
  public void setEnd(LocationRef end) {
    this.end = end;
  }

  public JourneyLeg departureTime(OffsetDateTime departureTime) {
    this.departureTime = departureTime;
    return this;
  }

  /**
   * Get departureTime
   * @return departureTime
   */
  @NotNull @Valid 
  @Schema(name = "departureTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("departureTime")
  public OffsetDateTime getDepartureTime() {
    return departureTime;
  }

  @JsonProperty("departureTime")
  public void setDepartureTime(OffsetDateTime departureTime) {
    this.departureTime = departureTime;
  }

  public JourneyLeg arrivalTime(OffsetDateTime arrivalTime) {
    this.arrivalTime = arrivalTime;
    return this;
  }

  /**
   * Get arrivalTime
   * @return arrivalTime
   */
  @NotNull @Valid 
  @Schema(name = "arrivalTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("arrivalTime")
  public OffsetDateTime getArrivalTime() {
    return arrivalTime;
  }

  @JsonProperty("arrivalTime")
  public void setArrivalTime(OffsetDateTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public JourneyLeg provider(@Nullable String provider) {
    this.provider = provider;
    return this;
  }

  /**
   * Carrier or provider name.
   * @return provider
   */
  
  @Schema(name = "provider", description = "Carrier or provider name.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("provider")
  public @Nullable String getProvider() {
    return provider;
  }

  @JsonProperty("provider")
  public void setProvider(@Nullable String provider) {
    this.provider = provider;
  }

  public JourneyLeg line(@Nullable String line) {
    this.line = line;
    return this;
  }

  /**
   * Optional line, flight number, or route code.
   * @return line
   */
  
  @Schema(name = "line", description = "Optional line, flight number, or route code.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("line")
  public @Nullable String getLine() {
    return line;
  }

  @JsonProperty("line")
  public void setLine(@Nullable String line) {
    this.line = line;
  }

  public JourneyLeg notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  @JsonProperty("notes")
  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JourneyLeg journeyLeg = (JourneyLeg) o;
    return Objects.equals(this.mode, journeyLeg.mode) &&
        Objects.equals(this.start, journeyLeg.start) &&
        Objects.equals(this.end, journeyLeg.end) &&
        Objects.equals(this.departureTime, journeyLeg.departureTime) &&
        Objects.equals(this.arrivalTime, journeyLeg.arrivalTime) &&
        Objects.equals(this.provider, journeyLeg.provider) &&
        Objects.equals(this.line, journeyLeg.line) &&
        Objects.equals(this.notes, journeyLeg.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, start, end, departureTime, arrivalTime, provider, line, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JourneyLeg {\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    departureTime: ").append(toIndentedString(departureTime)).append("\n");
    sb.append("    arrivalTime: ").append(toIndentedString(arrivalTime)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    line: ").append(toIndentedString(line)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

