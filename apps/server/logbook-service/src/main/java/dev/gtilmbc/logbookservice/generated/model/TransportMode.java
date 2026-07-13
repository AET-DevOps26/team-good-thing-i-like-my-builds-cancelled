package dev.gtilmbc.logbookservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets TransportMode
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:39.374777+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public enum TransportMode {
  
  TRAIN("TRAIN"),
  
  BUS("BUS"),
  
  TRAM("TRAM"),
  
  SUBWAY("SUBWAY"),
  
  FERRY("FERRY"),
  
  FLIGHT("FLIGHT"),
  
  CAR("CAR"),
  
  BIKE("BIKE"),
  
  WALK("WALK"),
  
  OTHER("OTHER");

  private final String value;

  TransportMode(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TransportMode fromValue(String value) {
    for (TransportMode b : TransportMode.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

