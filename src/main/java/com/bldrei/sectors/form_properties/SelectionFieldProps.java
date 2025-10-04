package com.bldrei.sectors.form_properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public record SelectionFieldProps<T>(boolean required, List<T> options) implements FormFieldProps {

  public static <V> SelectionFieldProps<HierarchicSelectionFieldTreeNode<V>> multilayer(boolean required, List<HierarchicSelectionFieldTreeNode<V>> values) {
    return new SelectionFieldProps<>(required, values);
  }
}
