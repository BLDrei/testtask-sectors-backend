package com.bldrei.sectors.controller;

import com.bldrei.sectors.dto.CurrentSessionPersonSectorFormResponse;
import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.dto.PersonSectorsFormProperties;
import com.bldrei.sectors.dto.TranslationsResponse;
import com.bldrei.sectors.i18n.Language;
import com.bldrei.sectors.service.PersonSectionsTranslationService;
import com.bldrei.sectors.service.PersonSectorFormPropertiesService;
import com.bldrei.sectors.service.PersonSectorsFormService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("person-sectors-form")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PersonSectorsFormController {
  private final PersonSectorFormPropertiesService personSectorFormPropertiesService;
  private final PersonSectionsTranslationService personSectionsTranslationService;

  private final PersonSectorsFormService personSectorsFormService;

  @GetMapping("form-properties")
  public PersonSectorsFormProperties getFormProperties() {
    return personSectorFormPropertiesService.getFormProperties();
  }

  @GetMapping("translations")
  public TranslationsResponse getSectionsTranslations(@RequestParam Language language) {
    return personSectionsTranslationService.getPersonSectorsFormTranslations(language);
  }

  @GetMapping
  public CurrentSessionPersonSectorFormResponse getCurrentSessionData(HttpSession httpSession) {
    return personSectorsFormService.get(httpSession)
      .map(CurrentSessionPersonSectorFormResponse::new)
      .orElseGet(() -> new CurrentSessionPersonSectorFormResponse(null));
  }

  @PostMapping
  public void savePersonSectors(
    @RequestBody @Valid PersonSectorsDto personSectorsDto,
    HttpSession httpSession
  ) {
    personSectorsFormService.save(personSectorsDto, httpSession);
  }
}
