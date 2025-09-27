package com.bldrei.sectors.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectorEntity {
  private long id;
  private String value;
  private long orderNo;
  private Long parentSectorId;
}
