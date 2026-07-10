package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.gtilmbc.routeservice.generated.model.TrainConnection;
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
 * Reference to a connection selected from route-service results.
 */

@Schema(name = "RouteReference", description = "Reference to a connection selected from route-service results.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T10:57:03.123918+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class RouteReference {

  private @Nullable String startStationId;

  private @Nullable String destinationStationId;

  @Valid
  private List<String> viaStationIds = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime requestedTime;

  private @Nullable TrainConnection selectedConnection;

  public RouteReference startStationId(@Nullable String startStationId) {
    this.startStationId = startStationId;
    return this;
  }

  /**
   * Get startStationId
   * @return startStationId
   */
  
  @Schema(name = "startStationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startStationId")
  public @Nullable String getStartStationId() {
    return startStationId;
  }

  @JsonProperty("startStationId")
  public void setStartStationId(@Nullable String startStationId) {
    this.startStationId = startStationId;
  }

  public RouteReference destinationStationId(@Nullable String destinationStationId) {
    this.destinationStationId = destinationStationId;
    return this;
  }

  /**
   * Get destinationStationId
   * @return destinationStationId
   */
  
  @Schema(name = "destinationStationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("destinationStationId")
  public @Nullable String getDestinationStationId() {
    return destinationStationId;
  }

  @JsonProperty("destinationStationId")
  public void setDestinationStationId(@Nullable String destinationStationId) {
    this.destinationStationId = destinationStationId;
  }

  public RouteReference viaStationIds(List<String> viaStationIds) {
    this.viaStationIds = viaStationIds;
    return this;
  }

  public RouteReference addViaStationIdsItem(String viaStationIdsItem) {
    if (this.viaStationIds == null) {
      this.viaStationIds = new ArrayList<>();
    }
    this.viaStationIds.add(viaStationIdsItem);
    return this;
  }

  /**
   * Get viaStationIds
   * @return viaStationIds
   */
  
  @Schema(name = "viaStationIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("viaStationIds")
  public List<String> getViaStationIds() {
    return viaStationIds;
  }

  @JsonProperty("viaStationIds")
  public void setViaStationIds(List<String> viaStationIds) {
    this.viaStationIds = viaStationIds;
  }

  public RouteReference requestedTime(@Nullable OffsetDateTime requestedTime) {
    this.requestedTime = requestedTime;
    return this;
  }

  /**
   * Get requestedTime
   * @return requestedTime
   */
  @Valid 
  @Schema(name = "requestedTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedTime")
  public @Nullable OffsetDateTime getRequestedTime() {
    return requestedTime;
  }

  @JsonProperty("requestedTime")
  public void setRequestedTime(@Nullable OffsetDateTime requestedTime) {
    this.requestedTime = requestedTime;
  }

  public RouteReference selectedConnection(@Nullable TrainConnection selectedConnection) {
    this.selectedConnection = selectedConnection;
    return this;
  }

  /**
   * Get selectedConnection
   * @return selectedConnection
   */
  @Valid 
  @Schema(name = "selectedConnection", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("selectedConnection")
  public @Nullable TrainConnection getSelectedConnection() {
    return selectedConnection;
  }

  @JsonProperty("selectedConnection")
  public void setSelectedConnection(@Nullable TrainConnection selectedConnection) {
    this.selectedConnection = selectedConnection;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RouteReference routeReference = (RouteReference) o;
    return Objects.equals(this.startStationId, routeReference.startStationId) &&
        Objects.equals(this.destinationStationId, routeReference.destinationStationId) &&
        Objects.equals(this.viaStationIds, routeReference.viaStationIds) &&
        Objects.equals(this.requestedTime, routeReference.requestedTime) &&
        Objects.equals(this.selectedConnection, routeReference.selectedConnection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startStationId, destinationStationId, viaStationIds, requestedTime, selectedConnection);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RouteReference {\n");
    sb.append("    startStationId: ").append(toIndentedString(startStationId)).append("\n");
    sb.append("    destinationStationId: ").append(toIndentedString(destinationStationId)).append("\n");
    sb.append("    viaStationIds: ").append(toIndentedString(viaStationIds)).append("\n");
    sb.append("    requestedTime: ").append(toIndentedString(requestedTime)).append("\n");
    sb.append("    selectedConnection: ").append(toIndentedString(selectedConnection)).append("\n");
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

