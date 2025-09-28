package com.bldrei.sectors.form_properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HierarchicSelectionFieldTreeNode<T> {
  private final T value;
  private final List<HierarchicSelectionFieldTreeNode<T>> subOptions;

  public static <T> HierarchicSelectionFieldTreeNode<T> node(T value, List<HierarchicSelectionFieldTreeNode<T>> children) {
    return new HierarchicSelectionFieldTreeNode<>(value, children);
  }

  public static <T> HierarchicSelectionFieldTreeNode<T> leaf(T value) {
    return new HierarchicSelectionFieldTreeNode<>(value, null);
  }
}
