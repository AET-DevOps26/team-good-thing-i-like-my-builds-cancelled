package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.gtilmbc.routeservice.generated.model.Station;
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
 * TrainSegment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class TrainSegment {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime departureTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime arrivalTime;

  private Station start;

  private Station end;

  @Valid
  private List<@Valid Station> stops = new ArrayList<>();

  public TrainSegment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TrainSegment(OffsetDateTime departureTime, OffsetDateTime arrivalTime, Station start, Station end) {
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.start = start;
    this.end = end;
  }

  public TrainSegment departureTime(OffsetDateTime departureTime) {
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

  public TrainSegment arrivalTime(OffsetDateTime arrivalTime) {
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

  public TrainSegment start(Station start) {
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
  public Station getStart() {
    return start;
  }

  @JsonProperty("start")
  public void setStart(Station start) {
    this.start = start;
  }

  public TrainSegment end(Station end) {
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
  public Station getEnd() {
    return end;
  }

  @JsonProperty("end")
  public void setEnd(Station end) {
    this.end = end;
  }

  public TrainSegment stops(List<@Valid Station> stops) {
    this.stops = stops;
    return this;
  }

  public TrainSegment addStopsItem(Station stopsItem) {
    if (this.stops == null) {
      this.stops = new ArrayList<>();
    }
    this.stops.add(stopsItem);
    return this;
  }

  /**
   * Get stops
   * @return stops
   */
  @Valid 
  @Schema(name = "stops", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stops")
  public List<@Valid Station> getStops() {
    return stops;
  }

  @JsonProperty("stops")
  public void setStops(List<@Valid Station> stops) {
    this.stops = stops;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrainSegment trainSegment = (TrainSegment) o;
    return Objects.equals(this.departureTime, trainSegment.departureTime) &&
        Objects.equals(this.arrivalTime, trainSegment.arrivalTime) &&
        Objects.equals(this.start, trainSegment.start) &&
        Objects.equals(this.end, trainSegment.end) &&
        Objects.equals(this.stops, trainSegment.stops);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureTime, arrivalTime, start, end, stops);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrainSegment {\n");
    sb.append("    departureTime: ").append(toIndentedString(departureTime)).append("\n");
    sb.append("    arrivalTime: ").append(toIndentedString(arrivalTime)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    stops: ").append(toIndentedString(stops)).append("\n");
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

