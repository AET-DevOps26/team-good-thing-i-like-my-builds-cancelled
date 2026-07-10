package dev.gtilmbc.routeservice.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.gtilmbc.routeservice.generated.model.LogbookEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LogbookEntryPage
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-03T13:35:38.337858+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogbookEntryPage {

  @Valid
  private List<@Valid LogbookEntry> items = new ArrayList<>();

  private Integer page;

  private Integer size;

  private Integer totalElements;

  public LogbookEntryPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogbookEntryPage(List<@Valid LogbookEntry> items, Integer page, Integer size, Integer totalElements) {
    this.items = items;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
  }

  public LogbookEntryPage items(List<@Valid LogbookEntry> items) {
    this.items = items;
    return this;
  }

  public LogbookEntryPage addItemsItem(LogbookEntry itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid LogbookEntry> getItems() {
    return items;
  }

  @JsonProperty("items")
  public void setItems(List<@Valid LogbookEntry> items) {
    this.items = items;
  }

  public LogbookEntryPage page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * minimum: 0
   * @return page
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  @JsonProperty("page")
  public void setPage(Integer page) {
    this.page = page;
  }

  public LogbookEntryPage size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * minimum: 1
   * @return size
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "size", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(Integer size) {
    this.size = size;
  }

  public LogbookEntryPage totalElements(Integer totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Get totalElements
   * minimum: 0
   * @return totalElements
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "totalElements", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalElements")
  public Integer getTotalElements() {
    return totalElements;
  }

  @JsonProperty("totalElements")
  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogbookEntryPage logbookEntryPage = (LogbookEntryPage) o;
    return Objects.equals(this.items, logbookEntryPage.items) &&
        Objects.equals(this.page, logbookEntryPage.page) &&
        Objects.equals(this.size, logbookEntryPage.size) &&
        Objects.equals(this.totalElements, logbookEntryPage.totalElements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, page, size, totalElements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogbookEntryPage {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
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

