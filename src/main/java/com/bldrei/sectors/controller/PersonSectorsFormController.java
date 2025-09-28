package com.bldrei.sectors.controller;

import com.bldrei.sectors.form_properties.HierarchicSelectionFieldTreeNode;
import com.bldrei.sectors.form_properties.SelectionFieldProps;
import com.bldrei.sectors.service.SectorFormPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("person-sectors")
@RequiredArgsConstructor
public class PersonSectorsFormController {
  private final SectorFormPropertiesService sectorFormPropertiesService;

  @GetMapping("form-properties")
  @ResponseStatus(HttpStatus.OK)
  public SelectionFieldProps<HierarchicSelectionFieldTreeNode<String>> getFormProperties() {
    //TODO: add info about other fields (name, checkbox)
    return sectorFormPropertiesService.getSectorClassifiers();
  }
}
