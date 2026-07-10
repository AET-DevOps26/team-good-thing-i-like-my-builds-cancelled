package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SuggestionToken
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class SuggestionToken {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TOKEN("token");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private String token;

  public SuggestionToken() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SuggestionToken(TypeEnum type, String token) {
    this.type = type;
    this.token = token;
  }

  public SuggestionToken type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SuggestionToken token(String token) {
    this.token = token;
    return this;
  }

  /**
   * A single text token to append to the inline suggestion.
   * @return token
   */
  @NotNull 
  @Schema(name = "token", example = " The castle", description = "A single text token to append to the inline suggestion.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  @JsonProperty("token")
  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SuggestionToken suggestionToken = (SuggestionToken) o;
    return Objects.equals(this.type, suggestionToken.type) &&
        Objects.equals(this.token, suggestionToken.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuggestionToken {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

