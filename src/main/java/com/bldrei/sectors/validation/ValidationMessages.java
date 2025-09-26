package com.bldrei.sectors.validation;

import java.time.LocalDate;
import java.util.List;

//you can take messages from here: https://github.com/hibernate/hibernate-validator/blob/main/engine/src/main/resources/org/hibernate/validator/ValidationMessages.properties
public class ValidationMessages {

  public static final String MISSING_VALUE_MSG = "Value has to be specified!";
  public static final String MUST_BE_NULL = "must be null";
  public static final String NOT_EMPTY_MSG = "must be not empty";
  public static final String NOT_BLANK_MSG = "must not be blank";
  public static final String FUTURE_OR_PRESENT = "must be a date in the present or in the future";
  public static final String SHOULD_NOT_CONTAIN_NULL = "should not contain null";


  public static String min(Number value) {
    return "must be greater than or equal to %s".formatted(value);
  }

  public static String max(Number value) {
    return "must be less than or equal to %s".formatted(value);
  }

  public static String maxScale(int maxScale) {
    return "numeric value out of bounds (.<%s digits> expected)".formatted(maxScale);
  }

  public static String equalTo(Number value) {
    return "must be equal to %s".formatted(value);
  }

  public static String size(Integer min, Integer max) {
    return "size must be between %s and %s".formatted(min, max);
  }

  public static String maxLength(int maxLength) {
    return size(1, maxLength);
  }

  private ValidationMessages() {
  }

  public static <T> String acceptedValues(List<T> acceptedValues) {
    return "can only be one of the following values: " + acceptedValues;
  }

  public static String maxDate(LocalDate maxDate) {
    return "must be less than or equal to %s".formatted(maxDate);
  }
}
