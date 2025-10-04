package com.bldrei.sectors.validation;

public class ValidationMessages {

  private ValidationMessages() {}

  public static final String MUST_NOT_BE_NULL = "must not be null";
  public static final String MUST_BE_TRUE = "must be true";
  public static final String NOT_EMPTY_MSG = "must not be empty";
  public static final String NOT_BLANK_MSG = "must not be blank";

  public static String size(Integer min, Integer max) {
    return "size must be between %s and %s".formatted(min, max);
  }

}
