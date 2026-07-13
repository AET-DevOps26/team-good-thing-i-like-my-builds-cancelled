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
 * TextUpdate
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class TextUpdate {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TEXT_UPDATE("text_update");

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

  private String textBefore;

  private @Nullable String textAfter;

  public TextUpdate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TextUpdate(TypeEnum type, String textBefore) {
    this.type = type;
    this.textBefore = textBefore;
  }

  public TextUpdate type(TypeEnum type) {
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

  public TextUpdate textBefore(String textBefore) {
    this.textBefore = textBefore;
    return this;
  }

  /**
   * Report text before the cursor.
   * @return textBefore
   */
  @NotNull 
  @Schema(name = "textBefore", example = "Today I arrived in Marburg and walked through the old town.", description = "Report text before the cursor.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("textBefore")
  public String getTextBefore() {
    return textBefore;
  }

  @JsonProperty("textBefore")
  public void setTextBefore(String textBefore) {
    this.textBefore = textBefore;
  }

  public TextUpdate textAfter(@Nullable String textAfter) {
    this.textAfter = textAfter;
    return this;
  }

  /**
   * Report text after the cursor, if any.
   * @return textAfter
   */
  
  @Schema(name = "textAfter", example = "The weather was perfect.", description = "Report text after the cursor, if any.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("textAfter")
  public @Nullable String getTextAfter() {
    return textAfter;
  }

  @JsonProperty("textAfter")
  public void setTextAfter(@Nullable String textAfter) {
    this.textAfter = textAfter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextUpdate textUpdate = (TextUpdate) o;
    return Objects.equals(this.type, textUpdate.type) &&
        Objects.equals(this.textBefore, textUpdate.textBefore) &&
        Objects.equals(this.textAfter, textUpdate.textAfter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, textBefore, textAfter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextUpdate {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    textBefore: ").append(toIndentedString(textBefore)).append("\n");
    sb.append("    textAfter: ").append(toIndentedString(textAfter)).append("\n");
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

