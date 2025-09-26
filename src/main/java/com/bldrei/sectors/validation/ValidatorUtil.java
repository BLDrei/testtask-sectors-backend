package com.bldrei.sectors.validation;

import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidatorUtil {
  private ValidatorUtil() {}

  public static boolean validateAndAppendConstraintMessages(List<FieldValidationInstruction<?>> validationInstruction, ConstraintValidatorContext context) {
    List<FieldValidationInstruction<?>> invalidFields = validationInstruction
      .stream()
      .filter(it -> !it.isValid_appendConstraintViolationMessagesIfInvalid(context))
      .toList();

    return invalidFields.isEmpty();
  }
}
