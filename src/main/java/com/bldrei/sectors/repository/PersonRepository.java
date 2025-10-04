package com.bldrei.sectors.repository;

import com.bldrei.sectors.entity.PersonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String FIND_NAME_BY_PERSON_ID = """
    SELECT
      p.name
    FROM person p
    WHERE deleted_at is null
    AND id = ?
    """;

  private static final String SAVE_NEW_PERSON = """
    INSERT INTO person (name)
    VALUES (?)
    """;

  private static final String UPDATE_PERSON = """
    UPDATE person set name = ?
    WHERE deleted_at is null
    AND id = ?
    """;

  public Optional<PersonEntity> findById(long id) {
    return jdbcTemplate.query(
      FIND_NAME_BY_PERSON_ID,
      (rs, _) -> {
        var entity = new PersonEntity();
        entity.setId(id);
        entity.setName(rs.getString("name"));
        return entity;
      },
      id
    ).stream().findFirst();
  }

  public long save(PersonEntity entity) {
    return jdbcTemplate.update(
      SAVE_NEW_PERSON,
      entity.getName()
    );
  }

  public void update(long id, PersonEntity personEntity) {
    jdbcTemplate.update(
      UPDATE_PERSON,
      personEntity.getName(), id
    );
  }
}
