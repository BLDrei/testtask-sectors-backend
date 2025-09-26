package com.bldrei.sectors.validation;

import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldValidationInstruction<T> {

  private final T value;
  @NonNull
  private final String name;
  private final boolean deactivated;
  private final boolean optional;
  @NonNull
  private final List<FieldValidator<T>> valueValidators;
  @NonNull
  private final List<String> resultOfAppliedValidatorsForActiveField;
  @NonNull
  private final List<String> resultOfAlwaysPerformedChecks;

  public static <T> FieldValidationInstructionBuilder<T> forValue(T value) {
    return new FieldValidationInstructionBuilder<>(value);
  }

  public boolean isValid_appendConstraintViolationMessagesIfInvalid(ConstraintValidatorContext context) {
    AtomicBoolean isValid = new AtomicBoolean(true);

    for (String msg : resultOfAlwaysPerformedChecks) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(msg)
        .addPropertyNode(name)
        .addConstraintViolation();
      isValid.set(false);
    }
    if (!isValid.get()) {
      return false;
    }

    if (deactivated && value == null) {
      return true;
    }
    else if (deactivated && value != null) {
      appendConstraintViolation(context, ValidationMessages.MUST_BE_NULL);
      return false;
    }

    if (value == null) {
      if (!optional) {
        appendConstraintViolation(context, ValidationMessages.MISSING_VALUE_MSG);
        isValid.set(false);
      }
    }
    else {
      getDefaultValidators().forEach(validator -> {
        if (!validator.isValid(value)) {
          appendConstraintViolation(context, validator.violationMessage());
          isValid.set(false);
        }
      });

      if (isValid.get()) {
        valueValidators.forEach(validator -> {
          if (!validator.isValid(value)) {
            appendConstraintViolation(context, validator.violationMessage());
            isValid.set(false);
          }
        });
      }
    }

    resultOfAppliedValidatorsForActiveField.forEach(msg -> {
      appendConstraintViolation(context, msg);
      isValid.set(false);
    });

    return isValid.get();
  }

  private List<FieldValidator<T>> getDefaultValidators() {
    return switch (value) {
      case List<?> list -> List.of((FieldValidator<T>) FieldValidator.LIST_DOES_NOT_CONTAIN_NULL, (FieldValidator<T>) FieldValidator.LIST_NOT_EMPTY);
      default -> Collections.emptyList();
    };
  }

  private void appendConstraintViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
      .addPropertyNode(name)
      .addConstraintViolation();
  }


  public static class FieldValidationInstructionBuilder<T> {
    private final T value;
    private String name;
    private boolean deactivated;
    private boolean optional;
    private final List<FieldValidator<T>> valueValidatorsWithViolationMessages = new ArrayList<>();
    private final List<String> resultOfAppliedValidatorsForActiveField = new ArrayList<>();
    private final List<String> resultOfAlwaysPerformedChecks = new ArrayList<>();

    private FieldValidationInstructionBuilder(T value) {
      this.value = value;
    }

    public FieldValidationInstructionBuilder<T> name(@NonNull String name) {
      this.name = name;
      return this;
    }

    /*
    If field is deactivated, none of the following matters

    By default, field is required
    If it's set as optional, and is null, validators will not be applied and validation will pass
    If it's set as optional, and it has value, validators will be applied
     */
    public FieldValidationInstructionBuilder<T> optional() {
      this.optional = true;
      return this;
    }

    /*
    By default, field is activated
    In that case, every possible validation will apply

    If you deactivate the field, then it will be expected to be null
    Only alwaysCheck() validation will apply
     */
    public FieldValidationInstructionBuilder<T> askedOnlyIf(boolean askedOnlyIf) {
      this.deactivated = !askedOnlyIf;
      return this;
    }

    /*
    Defines an additional validator that will be applied if value is not null and default validators have passed
    */
    public FieldValidationInstructionBuilder<T> validate(@NonNull Predicate<T> validator, @NonNull String message) {
      return validate(new FieldValidator<>(validator, message));
    }

    public FieldValidationInstructionBuilder<T> validate(@NonNull FieldValidator<T> fieldValidator) {
      this.valueValidatorsWithViolationMessages.add(fieldValidator);
      return this;
    }

    public FieldValidationInstructionBuilder<T> validateIf(boolean precondition, @NonNull Predicate<T> validator, @NonNull String message) {
      return validateIf(precondition, new FieldValidator<>(validator, message));
    }

    public FieldValidationInstructionBuilder<T> validateIf(boolean precondition, @NonNull FieldValidator<T> fieldValidator) {
      if (precondition) {
        validate(fieldValidator);
      }
      return this;
    }

    /*
    Applies a validation if field is active, regardless of whether the value is present

    Usage example: if group of fields is active, check that at least one of the fields is filled
    Should not be used for validating the value itself
    If you want to validate a value that depends on another field, consider validateIf that takes FieldValidator or one-parameter lambda
    */
    public FieldValidationInstructionBuilder<T> validate(@NonNull BooleanSupplier condition, @NonNull String message) {
      if (!condition.getAsBoolean()) {
        this.resultOfAppliedValidatorsForActiveField.add(message);
      }
      return this;
    }

    public FieldValidationInstruction<T> build() {
      return new FieldValidationInstruction<>(
        value,
        name,
        deactivated,
        optional,
        List.copyOf(valueValidatorsWithViolationMessages),
        List.copyOf(resultOfAppliedValidatorsForActiveField),
        List.copyOf(resultOfAlwaysPerformedChecks)
      );
    }

    @SuppressWarnings("unchecked")
    public FieldValidationInstructionBuilder<Integer> min(Integer minValue) {
      return (FieldValidationInstructionBuilder<Integer>) validate((FieldValidator<T>) FieldValidator.min(minValue));
    }

    @SuppressWarnings("unchecked")
    public FieldValidationInstructionBuilder<Integer> max(Integer maxValue) {
      return (FieldValidationInstructionBuilder<Integer>) validate((FieldValidator<T>) FieldValidator.max(maxValue));
    }

    public FieldValidationInstructionBuilder<T> acceptedValues(List<T> acceptedValues) {
      return validate(FieldValidator.acceptedValues(acceptedValues));
    }

    @SuppressWarnings("unchecked")
    public FieldValidationInstructionBuilder<BigDecimal> bigDecimal(BigDecimal min, BigDecimal max, int maxScale) {
      return (FieldValidationInstructionBuilder<BigDecimal>) validate((FieldValidator<T>) FieldValidator.min(min))
        .validate((FieldValidator<T>) FieldValidator.max(max))
        .validate((FieldValidator<T>) FieldValidator.maxScale(maxScale));
    }

    @SuppressWarnings("unchecked")
    public FieldValidationInstructionBuilder<LocalDate> futureOrPresent() {
      return (FieldValidationInstructionBuilder<LocalDate>) validate((FieldValidator<T>) FieldValidator.futureOrPresent());
    }
  }
}
