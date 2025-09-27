package com.bldrei.sectors.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationEntity {
  private String translationKey;
  private String language;
  private String translation;
}
