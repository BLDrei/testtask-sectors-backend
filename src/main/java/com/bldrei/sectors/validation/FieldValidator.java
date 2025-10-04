package com.bldrei.sectors.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldValidator<T> {
  private final Predicate<T> validator;
  private final String violationMessage;
  private final Supplier<String> violationMessageSupplier;

  public FieldValidator(Predicate<T> validator, String violationMessage) {
    this(validator, violationMessage, null);
  }

  public static final FieldValidator<List<?>> LIST_DOES_NOT_CONTAIN_NULL = new FieldValidator<>(list -> list.stream().noneMatch(Objects::isNull), ValidationMessages.SHOULD_NOT_CONTAIN_NULL);
  public static final FieldValidator<List<?>> LIST_NOT_EMPTY = new FieldValidator<>(list -> !list.isEmpty(), ValidationMessages.NOT_EMPTY_MSG);
  private static final FieldValidator<String> STRING_NOT_BLANK = new FieldValidator<>(string -> !string.isBlank(), ValidationMessages.NOT_BLANK_MSG);
  public boolean isValid(T value) {
    return validator.test(value);
  }

  public String violationMessage() {
    return Objects.requireNonNullElseGet(violationMessage, violationMessageSupplier);
  }

  public static <T> FieldValidator<List<T>> size(int exactSize) {
    return size(exactSize, exactSize);
  }

  public static <T> FieldValidator<List<T>> size(int minSize, int maxSize) {
    return new FieldValidator<>(value -> {
      long actualSize = value.size();
      return minSize <= actualSize && actualSize <= maxSize;
    }, ValidationMessages.size(minSize, maxSize));
  }

  public static FieldValidator<String> length(int minLength, int maxLength) {
    return new FieldValidator<>(value -> value.length() >= minLength && value.length() <= maxLength, ValidationMessages.size(minLength, maxLength));
  }

  public static FieldValidator<String> notBlank() {
    return STRING_NOT_BLANK;
  }

  public static FieldValidator<Boolean> mustBeTrue() {
    return new FieldValidator<>(value -> value, ValidationMessages.MUST_BE_TRUE);
  }
}
