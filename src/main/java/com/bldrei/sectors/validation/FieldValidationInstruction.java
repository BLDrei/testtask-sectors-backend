package com.bldrei.sectors.validation;

import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
    Defines an additional validator that will be applied if value is not null and default validators have passed
    */
    public FieldValidationInstructionBuilder<T> validate(@NonNull Predicate<T> validator, @NonNull String message) {
      return validate(new FieldValidator<>(validator, message));
    }

    public FieldValidationInstructionBuilder<T> validate(@NonNull FieldValidator<T> fieldValidator) {
      this.valueValidatorsWithViolationMessages.add(fieldValidator);
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

  }
}
