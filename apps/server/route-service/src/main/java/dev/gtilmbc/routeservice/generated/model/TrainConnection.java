package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.gtilmbc.routeservice.generated.model.TrainSegment;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * TrainConnection
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class TrainConnection {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime departureTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime arrivalTime;

  @Valid
  private List<@Valid TrainSegment> segments = new ArrayList<>();

  public TrainConnection() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TrainConnection(OffsetDateTime departureTime, OffsetDateTime arrivalTime, List<@Valid TrainSegment> segments) {
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.segments = segments;
  }

  public TrainConnection departureTime(OffsetDateTime departureTime) {
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

  public TrainConnection arrivalTime(OffsetDateTime arrivalTime) {
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

  public TrainConnection segments(List<@Valid TrainSegment> segments) {
    this.segments = segments;
    return this;
  }

  public TrainConnection addSegmentsItem(TrainSegment segmentsItem) {
    if (this.segments == null) {
      this.segments = new ArrayList<>();
    }
    this.segments.add(segmentsItem);
    return this;
  }

  /**
   * Get segments
   * @return segments
   */
  @NotNull @Valid 
  @Schema(name = "segments", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("segments")
  public List<@Valid TrainSegment> getSegments() {
    return segments;
  }

  @JsonProperty("segments")
  public void setSegments(List<@Valid TrainSegment> segments) {
    this.segments = segments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrainConnection trainConnection = (TrainConnection) o;
    return Objects.equals(this.departureTime, trainConnection.departureTime) &&
        Objects.equals(this.arrivalTime, trainConnection.arrivalTime) &&
        Objects.equals(this.segments, trainConnection.segments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureTime, arrivalTime, segments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrainConnection {\n");
    sb.append("    departureTime: ").append(toIndentedString(departureTime)).append("\n");
    sb.append("    arrivalTime: ").append(toIndentedString(arrivalTime)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
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

