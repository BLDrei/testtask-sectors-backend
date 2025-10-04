package com.bldrei.sectors.repository;

import com.bldrei.sectors.entity.PersonSectorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonSectorRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_SECTORS_BY_PERSON_ID = """
    SELECT ps.person_id, s.code
    FROM person_sector ps
    LEFT JOIN sector s
    ON s.id = ps.sector_id
    WHERE ps.deleted_at is null
    AND person_id = ?
    """;

  private static final String INSERT_PERSON_SECTOR_RELATION = """
    INSERT INTO person_sector (person_id, sector_id)
    VALUES (?, (SELECT id FROM sector s WHERE deleted_at is null and code = ?))
    """;

  private static final String DELETE_PERSON_SECTOR_RELATION = """
    UPDATE person_sector ps
    SET ps.deleted_at = SYSDATE
    WHERE ps.deleted_at is null
    AND ps.person_id = ?
    AND ps.sector_id = (select s.id from sector s where s.code = ?)
    """;

  public List<PersonSectorEntity> findAllSectorsByPersonId(long personId) {
    return jdbcTemplate.query(
      FIND_ALL_SECTORS_BY_PERSON_ID,
      (rs, _) -> {
        var entity = new PersonSectorEntity();
        entity.setPersonId(rs.getLong("person_id"));
        entity.setSectorCode(rs.getString("code"));
        return entity;
      },
      personId
    );
  }

  public void save(long personId, Collection<String> sectorCodes) {
    jdbcTemplate.batchUpdate(
      INSERT_PERSON_SECTOR_RELATION,
      sectorCodes.stream().map(sectorCode -> new Object[] {personId, sectorCode}).toList()
    );
  }

  public void delete(long personId, Collection<String> sectorsToRemove) {
    jdbcTemplate.batchUpdate(
      DELETE_PERSON_SECTOR_RELATION,
      sectorsToRemove.stream().map(sectorCode -> new Object[] {personId, sectorCode}).toList()
    );
  }
}
