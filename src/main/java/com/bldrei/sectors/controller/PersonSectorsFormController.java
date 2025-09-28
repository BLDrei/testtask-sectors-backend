package com.bldrei.sectors.controller;

import com.bldrei.sectors.dto.TranslationsResponse;
import com.bldrei.sectors.form_properties.HierarchicSelectionFieldTreeNode;
import com.bldrei.sectors.form_properties.SelectionFieldProps;
import com.bldrei.sectors.i18n.Language;
import com.bldrei.sectors.service.PersonSectionsTranslationService;
import com.bldrei.sectors.service.SectorFormPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("person-sectors")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PersonSectorsFormController {
  private final SectorFormPropertiesService sectorFormPropertiesService;
  private final PersonSectionsTranslationService personSectionsTranslationService;

  @GetMapping("form-properties")
  @ResponseStatus(HttpStatus.OK)
  public SelectionFieldProps<HierarchicSelectionFieldTreeNode<String>> getFormProperties() {
    //TODO: add info about other fields (name, checkbox)
    return sectorFormPropertiesService.getSectorClassifiers();
  }

  @GetMapping("translations")
  @ResponseStatus(HttpStatus.OK)
  public TranslationsResponse getSectionsTranslations(@RequestParam Language language) {
    return personSectionsTranslationService.getPersonSectorsFormTranslations(language);
  }
}
