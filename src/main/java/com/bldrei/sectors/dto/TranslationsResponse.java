package com.bldrei.sectors.dto;

import java.util.Map;

public record TranslationsResponse(
  Map<String, String> translations
) {
}
