package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

public class LgtmGradeValue extends AbstractValue<LgtmGrade> {

  /**
   * The value.
   */
  private final LgtmGrade value;

  /**
   * Initializes an {@link LgtmGradeValue} for a feature.
   *
   * @param feature The feature.
   * @param value The value.
   */
  @JsonCreator
  public LgtmGradeValue(
      @JsonProperty("feature") Feature feature,
      @JsonProperty("value") LgtmGrade value) {

    super(feature);

    Objects.requireNonNull(value, "Value can't be null!");
    this.value = value;
  }

  @Override
  @JsonGetter("value")
  public LgtmGrade get() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof LgtmGradeValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LgtmGradeValue enumValue = (LgtmGradeValue) o;
    return Objects.equals(value, enumValue.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
