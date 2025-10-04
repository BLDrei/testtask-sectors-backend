package com.bldrei.sectors.dto;

import com.bldrei.sectors.validator.ValidPersonSectors;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@ValidPersonSectors
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonSectorsDto(
  String name,
  List<String> sectors,
  Boolean agreeToTerms
) {}

//note: figured out that custom annotation (@ValidPersonSectors) is not needed, as in this concrete scenario field-based annotations can cover it
//here is example how it could've been done without custom annotation
//
//public record PersonSectorsDto(
//  @NotBlank
//  @Size(min = PersonSectorsFormConfig.NAME_MIN_LENGTH, max = PersonSectorsFormConfig.NAME_MAX_LENGTH)
//  String name,
//  @NotEmpty
//  List<@NotBlank String> sectors,
//  @AssertTrue
//  Boolean agreeToTerms
//) {}
