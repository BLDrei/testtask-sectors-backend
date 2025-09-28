package com.bldrei.sectors.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectorEntity {
  private String code;
  private int orderNo;
  private String parentSectorCode;

  private List<SectorEntity> childSectors;
}
