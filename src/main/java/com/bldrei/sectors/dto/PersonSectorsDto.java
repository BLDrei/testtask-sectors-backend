package com.bldrei.sectors.dto;

import com.bldrei.sectors.config.PersonSectorsFormConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonSectorsDto(
  @NotBlank
  @Size(min = PersonSectorsFormConfig.NAME_MIN_LENGTH, max = PersonSectorsFormConfig.NAME_MAX_LENGTH)
  String name,
  @NotEmpty
  List<@NotBlank String> sectors,
  @NotNull
  @AssertTrue
  Boolean agreeToTerms
) {}
