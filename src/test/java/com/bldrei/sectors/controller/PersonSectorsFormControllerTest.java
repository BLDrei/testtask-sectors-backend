package com.bldrei.sectors.controller;

import com.bldrei.sectors.Launcher;
import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.service.PersonSectorsFormService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = Launcher.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-dev.properties")
public class PersonSectorsFormControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PersonSectorsFormService personSectorsFormService;

  @Test
  public void testGetCurrentSessionData_whenThereIsData() throws Exception {
    var mockedSession = new MockHttpSession();
    mockedSession.setAttribute("personId", 123L);

    when(personSectorsFormService.get(any()))
      .thenReturn(Optional.of(new PersonSectorsDto("SPONGE BOB", List.of("1", "121"), null)));

    mockMvc.perform(get("/person-sectors-form").session(mockedSession))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        {
          "data": {
            "name": "SPONGE BOB",
            "sectors": ["1", "121"]
          }
        }
        """));
  }

  @Test
  public void testGetCurrentSessionData_whenThereIsNoData() throws Exception {
    var mockedSession = new MockHttpSession();

    when(personSectorsFormService.get(any()))
      .thenReturn(Optional.empty());

    mockMvc.perform(get("/person-sectors-form").session(mockedSession))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        {
          "data": null
        }
        """));
  }

  @Test
  public void testSavePerson_didNotAgreeToTerms_notOk() throws Exception {
    mockMvc.perform(post("/person-sectors-form")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
          "name": "GARY",
          "sectors": ["121", "1"],
          "agreeToTerms": false
        }
        """))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testSavePerson_agreedToTerms_success() throws Exception {
    var mockedSession = new MockHttpSession();

    mockMvc.perform(post("/person-sectors-form")
        .session(mockedSession)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
          "name": "GARY",
          "sectors": ["121", "1"],
          "agreeToTerms": true
        }
        """))
      .andExpect(status().isOk());

    var dtoCaptor = ArgumentCaptor.forClass(PersonSectorsDto.class);
    doNothing().when(personSectorsFormService).save(any(), any());
    verify(personSectorsFormService, times(1)).save(dtoCaptor.capture(), any());

    assertEquals("GARY", dtoCaptor.getValue().name());
    assertEquals(List.of("121", "1"), dtoCaptor.getValue().sectors());
    assertEquals(true, dtoCaptor.getValue().agreeToTerms());
  }

  @Test
  public void testGetFormProperties() throws Exception {
    mockMvc.perform(get("/person-sectors-form/form-properties"))
      .andExpect(status().isOk())
      .andExpect(content().json(Files.readString(Path.of("src/test/resources/form_properties/person-sector-form-properties.json"))));
  }
}
