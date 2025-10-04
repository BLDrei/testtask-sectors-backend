package com.bldrei.sectors.validation;

//you can take messages from here: https://github.com/hibernate/hibernate-validator/blob/main/engine/src/main/resources/org/hibernate/validator/ValidationMessages.properties
public class ValidationMessages {

  public static final String MISSING_VALUE_MSG = "Value has to be specified!";
  public static final String MUST_BE_NULL = "must be null";
  public static final String MUST_BE_TRUE = "must be true";
  public static final String NOT_EMPTY_MSG = "must be not empty";
  public static final String NOT_BLANK_MSG = "must not be blank";
  public static final String SHOULD_NOT_CONTAIN_NULL = "should not contain null";

  public static String size(Integer min, Integer max) {
    return "size must be between %s and %s".formatted(min, max);
  }

  private ValidationMessages() {
  }
}
