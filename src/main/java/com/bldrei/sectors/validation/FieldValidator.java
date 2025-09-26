package com.bldrei.sectors.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
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

  public FieldValidator(Predicate<T> validator, Supplier<String> violationMessageSupplier) {
    this(validator, null, violationMessageSupplier);
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

  public static FieldValidator<Integer> min(Integer minValue) {
    return new FieldValidator<>(value -> minValue.compareTo(value) <= 0, ValidationMessages.min(minValue));
  }

  public static FieldValidator<BigDecimal> min(BigDecimal minValue) {
    return new FieldValidator<>(value -> minValue.compareTo(value) <= 0, ValidationMessages.min(minValue));
  }

  public static FieldValidator<Integer> max(Integer maxValue) {
    return new FieldValidator<>(value -> maxValue.compareTo(value) >= 0, ValidationMessages.max(maxValue));
  }

  public static FieldValidator<BigDecimal> max(BigDecimal maxValue) {
    return new FieldValidator<>(value -> maxValue.compareTo(value) >= 0, ValidationMessages.max(maxValue));
  }

  public static FieldValidator<BigDecimal> maxScale(int maxScale) {
    return new FieldValidator<>(bd -> bd.scale() <= maxScale, ValidationMessages.maxScale(maxScale));
  }

  public static <T> FieldValidator<T> equalTo(T expected) {
    return switch (expected) {
      case BigDecimal bdExp -> new FieldValidator<>(value -> bdExp.compareTo((BigDecimal) value) == 0, ValidationMessages.equalTo(bdExp));
      case BigInteger biExp -> new FieldValidator<>(value -> biExp.compareTo((BigInteger) value) == 0, ValidationMessages.equalTo(biExp));
      case Integer iExp -> new FieldValidator<>(value -> iExp.compareTo((Integer) value) == 0, ValidationMessages.equalTo(iExp));
      case Double dExp -> new FieldValidator<>(value -> dExp.compareTo((Double) value) == 0, ValidationMessages.equalTo(dExp));
      default -> throw new NotImplementedException(
        "FieldValidator::equalTo doesn't support " + expected.getClass() + ", but you can easily implement that");
    };
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

  public static FieldValidator<String> maxLength(int maxLength) {
    return new FieldValidator<>(value -> value.length() <= maxLength, ValidationMessages.size(1, maxLength));
  }

  public static FieldValidator<String> notBlank() {
    return STRING_NOT_BLANK;
  }

  public static <T> FieldValidator<T> acceptedValues(List<T> acceptedValues) {
    return new FieldValidator<>(acceptedValues::contains, () -> ValidationMessages.acceptedValues(acceptedValues));
  }

  public static FieldValidator<LocalDate> futureOrPresent() {
    return new FieldValidator<>(value -> !value.isBefore(LocalDate.now()), ValidationMessages.FUTURE_OR_PRESENT);
  }

  public static FieldValidator<LocalDate> maxDate(Supplier<LocalDate> maxDate) {
    return new FieldValidator<>(value -> !value.isAfter(maxDate.get()), () -> ValidationMessages.maxDate(maxDate.get()));
  }
}
