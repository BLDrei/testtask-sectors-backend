package com.bldrei.sectors.service;

import com.bldrei.sectors.dto.TranslationsResponse;
import com.bldrei.sectors.i18n.Language;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static javax.management.timer.Timer.ONE_MINUTE;

@Service
@AllArgsConstructor
@Slf4j
public class PersonSectionsTranslationService {
  private final TranslationService translationService;

  private static final long THREE_MINUTES = 3 * ONE_MINUTE;

  @Cacheable("personSectorsFormTranslations")
  public TranslationsResponse getPersonSectorsFormTranslations(Language language) {
    var allTranslations = new HashMap<>(translationService.getAllTranslationsByGroupAndLanguage("sector", language));
    allTranslations.putAll(translationService.getAllTranslationsByGroupAndLanguage("person-sector-form", language));

    return new TranslationsResponse(allTranslations);
  }

  @Scheduled(fixedDelay = THREE_MINUTES)
  @CacheEvict(value = {"personSectorsFormTranslations"})
  public void clearCache() {
    log.debug("Person-sectors form translations cache cleared");
  }
}
