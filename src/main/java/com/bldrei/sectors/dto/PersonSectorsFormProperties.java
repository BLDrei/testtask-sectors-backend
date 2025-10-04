package com.bldrei.sectors.dto;

import com.bldrei.sectors.form_properties.BooleanFieldProps;
import com.bldrei.sectors.form_properties.HierarchicSelectionFieldTreeNode;
import com.bldrei.sectors.form_properties.SelectionFieldProps;
import com.bldrei.sectors.form_properties.TextFieldProps;

public record PersonSectorsFormProperties(
  TextFieldProps name,
  SelectionFieldProps<HierarchicSelectionFieldTreeNode<String>> sectors,
  BooleanFieldProps agreeToTerms
) {

}
