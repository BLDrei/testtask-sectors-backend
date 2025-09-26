package com.bldrei.sectors.form_properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record TextFieldProps(Integer maxlength) implements FormFieldProps {}
