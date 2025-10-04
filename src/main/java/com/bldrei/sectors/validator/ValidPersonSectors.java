package com.bldrei.sectors.validator;

import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.validation.FieldValidationInstruction;
import com.bldrei.sectors.validation.ValidationMessages;
import com.bldrei.sectors.validation.ValidatorUtil;
import com.bldrei.sectors.validator.ValidPersonSectors.PersonSectorsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static com.bldrei.sectors.config.PersonSectorsFormConfig.NAME_MAX_LENGTH;
import static com.bldrei.sectors.config.PersonSectorsFormConfig.NAME_MIN_LENGTH;
import static com.bldrei.sectors.validation.FieldValidator.length;
import static com.bldrei.sectors.validation.FieldValidator.mustBeTrue;
import static com.bldrei.sectors.validation.FieldValidator.notBlank;

@Constraint(validatedBy = PersonSectorsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonSectors {
  String message() default "Invalid person sectors form";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};


  class PersonSectorsValidator implements ConstraintValidator<ValidPersonSectors, PersonSectorsDto> {
    @Override
    public boolean isValid(PersonSectorsDto dto, ConstraintValidatorContext ctx) {
      List<FieldValidationInstruction<?>> validationInstruction = List.of(
        FieldValidationInstruction.forValue(dto.name())
          .name("name")
          .validate(notBlank())
          .validate(length(NAME_MIN_LENGTH, NAME_MAX_LENGTH))
          .build(),
        FieldValidationInstruction.forValue(dto.sectors())
          .name("sectors")
          .validate(list -> list.stream().noneMatch(String::isBlank), ValidationMessages.NOT_BLANK_MSG)
          .build(),
        FieldValidationInstruction.forValue(dto.agreeToTerms())
          .name("agreeToTerms")
          .validate(mustBeTrue())
          .build()
      );
      return ValidatorUtil.validateAndAppendConstraintMessages(validationInstruction, ctx);
    }
  }

}
