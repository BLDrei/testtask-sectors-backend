package com.bldrei.sectors.service;

import com.bldrei.sectors.entity.SectorEntity;
import com.bldrei.sectors.form_properties.HierarchicSelectionFieldTreeNode;
import com.bldrei.sectors.form_properties.SelectionFieldProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectorFormPropertiesService {
  private final SectorClassifiersService sectorClassifiersService;

  private static HierarchicSelectionFieldTreeNode<String> mapSectorEntityToHierarchicTreeNode(SectorEntity sectorEntity) {
    return switch (sectorEntity) {
      case SectorEntity e when e.getChildSectors() == null -> HierarchicSelectionFieldTreeNode.leaf(e.getCode());
      case SectorEntity e -> HierarchicSelectionFieldTreeNode.node(
        e.getCode(),
        e.getChildSectors().stream().map(SectorFormPropertiesService::mapSectorEntityToHierarchicTreeNode).toList()
      );
    };
  }
  public SelectionFieldProps<HierarchicSelectionFieldTreeNode<String>> getSectorClassifiers() {
    var c = sectorClassifiersService.getSectorsToSubSectorsRelation();
    var b = c.stream()
      .map(SectorFormPropertiesService::mapSectorEntityToHierarchicTreeNode)
      .toList();
    return SelectionFieldProps.multilayer(b);
  }

}
