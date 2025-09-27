package com.bldrei.sectors.repository;

import com.bldrei.sectors.entity.SectorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SectorRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_SECTORS = """
    SELECT
      s.id,
      s.value,
      s.order_no,
      s.parent_sector_id
    FROM sector s
    WHERE deleted_at = null
    """;

  public List<SectorEntity> findAll() {
    return jdbcTemplate.queryForStream(
      FIND_ALL_SECTORS,
      (rs, rowNum) -> {
        var entity = new SectorEntity();
        entity.setId(rs.getLong("id"));
        entity.setValue(rs.getString("value"));
        entity.setOrderNo(rs.getLong("order_no"));
        entity.setParentSectorId(rs.getLong("parent_sector_id"));
        return entity;
      }
    ).toList();
  }
}
