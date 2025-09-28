package com.bldrei.sectors.service;

import com.bldrei.sectors.entity.TranslationEntity;
import com.bldrei.sectors.i18n.Language;
import com.bldrei.sectors.repository.TranslationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {
  private final TranslationRepository translationRepository;

  public Map<String, String> getAllTranslationsByGroupAndLanguage(String translationGroup, Language language) {
    var sectorEntities = translationRepository.findAllByTranslationGroupAndLanguage(translationGroup, language);

    return sectorEntities.stream().collect(Collectors.toUnmodifiableMap(
      it -> "%s.%s".formatted(translationGroup, it.getTranslationKey()),
      TranslationEntity::getTranslation
    ));
  }
}
