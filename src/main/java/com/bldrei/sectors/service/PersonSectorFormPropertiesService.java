package com.bldrei.sectors.service;

import com.bldrei.sectors.config.PersonSectorsFormConfig;
import com.bldrei.sectors.dto.PersonSectorsFormProperties;
import com.bldrei.sectors.entity.SectorEntity;
import com.bldrei.sectors.form_properties.BooleanFieldProps;
import com.bldrei.sectors.form_properties.HierarchicSelectionFieldTreeNode;
import com.bldrei.sectors.form_properties.SelectionFieldProps;
import com.bldrei.sectors.form_properties.TextFieldProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonSectorFormPropertiesService {
  private final SectorClassifiersService sectorClassifiersService;

  public PersonSectorsFormProperties getFormProperties() {
    return new PersonSectorsFormProperties(
      new TextFieldProps(true, PersonSectorsFormConfig.NAME_MIN_LENGTH, PersonSectorsFormConfig.NAME_MAX_LENGTH),
      getSectorClassifiers(),
      new BooleanFieldProps(true)
    );
  }

  private SelectionFieldProps<HierarchicSelectionFieldTreeNode<String>> getSectorClassifiers() {
    var c = sectorClassifiersService.getSectorsToSubSectorsRelation();
    var b = c.stream()
      .map(PersonSectorFormPropertiesService::mapSectorEntityToHierarchicTreeNode)
      .toList();
    return SelectionFieldProps.multilayer(true, b);
  }

  private static HierarchicSelectionFieldTreeNode<String> mapSectorEntityToHierarchicTreeNode(SectorEntity sectorEntity) {
    return switch (sectorEntity) {
      case SectorEntity e when e.getChildSectors() == null -> HierarchicSelectionFieldTreeNode.leaf(e.getCode());
      case SectorEntity e -> HierarchicSelectionFieldTreeNode.node(
        e.getCode(),
        e.getChildSectors().stream().map(PersonSectorFormPropertiesService::mapSectorEntityToHierarchicTreeNode).toList()
      );
    };
  }

}
