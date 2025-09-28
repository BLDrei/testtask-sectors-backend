package com.bldrei.sectors.service;

import com.bldrei.sectors.entity.SectorEntity;
import com.bldrei.sectors.repository.SectorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.management.timer.Timer.ONE_MINUTE;

@Service
@AllArgsConstructor
@Slf4j
public class SectorClassifiersService {
  private final SectorRepository sectorRepository;

  private static final long THREE_MINUTES = 3 * ONE_MINUTE;

  @Cacheable("sectorsToSubSectorsRelation")
  public List<SectorEntity> getSectorsToSubSectorsRelation() {
    var sectorEntities = sectorRepository.findAll();
    var codeToEntityShortcutMap = sectorEntities.stream().collect(Collectors.toMap(
      SectorEntity::getCode,
      Function.identity()
    ));

    sectorEntities.stream()
      .filter(sectorEntity -> sectorEntity.getParentSectorCode() != null)
      .forEach(sectorEntity -> {
        var parentEntity = codeToEntityShortcutMap.get(sectorEntity.getParentSectorCode());
        if (parentEntity.getChildSectors() == null) {
          parentEntity.setChildSectors(new LinkedList<>());
        }
        parentEntity.getChildSectors().add(sectorEntity);
      });

    var rootSectorEntries = sectorEntities.stream().filter(it -> it.getParentSectorCode() == null).toList();
    return rootSectorEntries;
  }

  @Scheduled(fixedDelay = THREE_MINUTES)
  @CacheEvict(value = {"sectorsToSubSectorsRelation"})
  public void clearSectorsToSubSectorsRelation() {
    log.debug("Sectors to sub-sectors relation cache cleared");
  }
}
