package com.bldrei.sectors.repository;

import com.bldrei.sectors.entity.TranslationEntity;
import com.bldrei.sectors.i18n.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TranslationRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_TRANSLATIONS_BY_GROUP = """
    SELECT
      t.translation_key,
      t.language,
      t.translation
    FROM translation t
    WHERE deleted_at is null
    AND t.translation_group = ?
    AND t.language = ?
    """;

  public List<TranslationEntity> findAllByTranslationGroupAndLanguage(String translationGroup, Language language) {
    return jdbcTemplate.queryForStream(
      FIND_ALL_TRANSLATIONS_BY_GROUP,
      (rs, _) -> {
        var entity = new TranslationEntity();
        entity.setTranslationKey(rs.getString("translation_key"));
        entity.setLanguage(rs.getString("language"));
        entity.setTranslation(rs.getString("translation"));
        return entity;
      },
      translationGroup, language.name()
    ).toList();
  }
}
