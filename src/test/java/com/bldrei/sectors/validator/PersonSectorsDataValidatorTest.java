package com.bldrei.sectors.validator;

import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.validation.ValidationMessages;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bldrei.sectors.StringHelper.repeat;

public class PersonSectorsDataValidatorTest extends AbstractValidatorTest<PersonSectorsDto> {

  @Test
  void allFieldsRequired() {
    assertViolationMessagesPerField(
      new PersonSectorsDto(null, null, null),
      Map.of(
        "name", List.of(ValidationMessages.MISSING_VALUE_MSG),
        "sectors", List.of(ValidationMessages.MISSING_VALUE_MSG),
        "agreeToTerms", List.of(ValidationMessages.MISSING_VALUE_MSG)
      )
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1"), true),
      Map.of()
    );
  }

  @Test
  void name_mustNotBeBlankOrTooLongTooShort() {
    assertViolationMessagesPerField(
      new PersonSectorsDto("      ", List.of("1"), true),
      Map.of("name", List.of(ValidationMessages.NOT_BLANK_MSG))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("A", List.of("1"), true),
      Map.of("name", List.of(ValidationMessages.size(2, 400)))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("AA", List.of("1"), true),
      Map.of()
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto(repeat("A", 400), List.of("1"), true),
      Map.of()
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto(repeat("A", 401), List.of("1"), true),
      Map.of("name", List.of(ValidationMessages.size(2, 400)))
    );
  }

  @Test
  void sections_mustNotBeEmptyOrContainNullsOrContainBlankStrings() {
    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of(), true),
      Map.of("sectors", List.of(ValidationMessages.NOT_EMPTY_MSG))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1", "   "), true),
      Map.of("sectors", List.of(ValidationMessages.NOT_BLANK_MSG))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", Arrays.asList("1", null), true),
      Map.of("sectors", List.of(ValidationMessages.SHOULD_NOT_CONTAIN_NULL))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1"), true),
      Map.of()
    );
  }

  @Test
  void agreeToTerms_mustBeTrue() {
    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1"), true),
      Map.of()
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1"), null),
      Map.of("agreeToTerms", List.of(ValidationMessages.MISSING_VALUE_MSG))
    );

    assertViolationMessagesPerField(
      new PersonSectorsDto("Ashley", List.of("1"), false),
      Map.of("agreeToTerms", List.of(ValidationMessages.MUST_BE_TRUE))
    );
  }

}
