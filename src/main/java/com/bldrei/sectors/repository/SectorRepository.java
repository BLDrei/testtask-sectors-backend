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
      s.code,
      s.order_no,
      s.parent_sector_code
    FROM sector s
    WHERE deleted_at is null
    """;

  public List<SectorEntity> findAll() {
    return jdbcTemplate.queryForStream(
      FIND_ALL_SECTORS,
      (rs, _) -> {
        var entity = new SectorEntity();
        entity.setCode(rs.getString("code"));
        entity.setOrderNo(rs.getInt("order_no"));
        entity.setParentSectorCode(rs.getString("parent_sector_code"));
        return entity;
      }
    ).toList();
  }
}
