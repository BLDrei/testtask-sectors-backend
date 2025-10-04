package com.bldrei.sectors.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractValidatorTest<T> {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  protected void assertViolationMessagesPerField(T dto, Map<String, List<String>> expectedViolationMessages) {
    var actualViolations = validator.validate(dto)
      .stream()
      .map(it -> new Pair<>(it.getPropertyPath().toString(), it.getMessage()))
      .collect(groupingBy(Pair::a, mapping(Pair::b, toUnmodifiableList())));

    assertEquals(expectedViolationMessages, actualViolations);
  }

  private record Pair<A, B>(A a, B b) {}

}
