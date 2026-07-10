package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LocationRef
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T10:57:03.123918+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LocationRef {

  private String city;

  private @Nullable String countryCode;

  private @Nullable String stationId;

  private @Nullable String stationName;

  private @Nullable Double latitude;

  private @Nullable Double longitude;

  public LocationRef() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LocationRef(String city) {
    this.city = city;
  }

  public LocationRef city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  @NotNull 
  @Schema(name = "city", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  public LocationRef countryCode(@Nullable String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  /**
   * ISO 3166-1 alpha-2 country code.
   * @return countryCode
   */
  @Size(min = 2, max = 2) 
  @Schema(name = "countryCode", example = "DE", description = "ISO 3166-1 alpha-2 country code.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("countryCode")
  public @Nullable String getCountryCode() {
    return countryCode;
  }

  @JsonProperty("countryCode")
  public void setCountryCode(@Nullable String countryCode) {
    this.countryCode = countryCode;
  }

  public LocationRef stationId(@Nullable String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * Station id from route service when available.
   * @return stationId
   */
  
  @Schema(name = "stationId", description = "Station id from route service when available.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stationId")
  public @Nullable String getStationId() {
    return stationId;
  }

  @JsonProperty("stationId")
  public void setStationId(@Nullable String stationId) {
    this.stationId = stationId;
  }

  public LocationRef stationName(@Nullable String stationName) {
    this.stationName = stationName;
    return this;
  }

  /**
   * Human-readable station name.
   * @return stationName
   */
  
  @Schema(name = "stationName", description = "Human-readable station name.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stationName")
  public @Nullable String getStationName() {
    return stationName;
  }

  @JsonProperty("stationName")
  public void setStationName(@Nullable String stationName) {
    this.stationName = stationName;
  }

  public LocationRef latitude(@Nullable Double latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * Get latitude
   * @return latitude
   */
  
  @Schema(name = "latitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latitude")
  public @Nullable Double getLatitude() {
    return latitude;
  }

  @JsonProperty("latitude")
  public void setLatitude(@Nullable Double latitude) {
    this.latitude = latitude;
  }

  public LocationRef longitude(@Nullable Double longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * Get longitude
   * @return longitude
   */
  
  @Schema(name = "longitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("longitude")
  public @Nullable Double getLongitude() {
    return longitude;
  }

  @JsonProperty("longitude")
  public void setLongitude(@Nullable Double longitude) {
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationRef locationRef = (LocationRef) o;
    return Objects.equals(this.city, locationRef.city) &&
        Objects.equals(this.countryCode, locationRef.countryCode) &&
        Objects.equals(this.stationId, locationRef.stationId) &&
        Objects.equals(this.stationName, locationRef.stationName) &&
        Objects.equals(this.latitude, locationRef.latitude) &&
        Objects.equals(this.longitude, locationRef.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, countryCode, stationId, stationName, latitude, longitude);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationRef {\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    stationName: ").append(toIndentedString(stationName)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
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

