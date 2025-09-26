package com.bldrei.sectors.form_properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record SelectionFieldProps<T>(List<T> options) implements FormFieldProps {

  public static <E extends Enum<E>> SelectionFieldProps<String> ofEnums(List<E> values) {
    return new SelectionFieldProps<>(values.stream().map(E::name).toList());
  }
}
