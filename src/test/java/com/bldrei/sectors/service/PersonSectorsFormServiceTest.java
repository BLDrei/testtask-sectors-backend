package com.bldrei.sectors.service;

import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.entity.PersonEntity;
import com.bldrei.sectors.entity.PersonSectorEntity;
import com.bldrei.sectors.repository.PersonRepository;
import com.bldrei.sectors.repository.PersonSectorRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonSectorsFormServiceTest {

  @Mock
  private PersonRepository personRepository;

  @Mock
  private PersonSectorRepository personSectorRepository;

  @Mock
  private HttpSession session;

  @InjectMocks
  private PersonSectorsFormService service;

  private PersonEntity personEntity;

  @BeforeEach
  void setup() {
    personEntity = new PersonEntity();
    personEntity.setId(123L);
    personEntity.setName("Alice");
  }

  @Test
  void get_shouldReturnEmpty_whenNoPersonIdInSession() {
    when(session.getAttribute("personId")).thenReturn(null);

    var result = service.get(session);

    assertThat(result).isEmpty();
    verifyNoInteractions(personRepository);
    verifyNoInteractions(personSectorRepository);
  }

  @Test
  void get_shouldReturnDto_whenPersonExists() {
    when(session.getAttribute("personId")).thenReturn(123L);
    when(personRepository.findById(123L)).thenReturn(Optional.of(personEntity));
    when(personSectorRepository.findAllSectorsByPersonId(123L))
      .thenReturn(List.of(new PersonSectorEntity(123L, "121")));

    var result = service.get(session);

    assertThat(result).isPresent();
    assertThat(result.get().name()).isEqualTo("Alice");
    assertThat(result.get().sectors()).containsExactly("121");
  }

  @Test
  void save_shouldCreateNewPerson_whenNoSessionId() {
    when(session.getAttribute("personId")).thenReturn(null);
    when(personRepository.save(any(PersonEntity.class))).thenReturn(99L);

    var dto = new PersonSectorsDto("Bob", List.of("1", "121"), true);

    service.save(dto, session);

    verify(personRepository).save(argThat(p -> p.getName().equals("Bob")));
    verify(personSectorRepository).save(eq(99L), eq(List.of("1", "121")));
    verify(session).setAttribute("personId", 99L);
  }

  @Test
  void save_shouldUpdateExistingPerson_whenSessionIdPresent() {
    when(session.getAttribute("personId")).thenReturn(123L);
    when(personRepository.findById(123L)).thenReturn(Optional.of(personEntity));
    when(personSectorRepository.findAllSectorsByPersonId(123L))
      .thenReturn(List.of(
        new PersonSectorEntity(123L, "IT"),
        new PersonSectorEntity(123L, "Finance")
      ));

    var dto = new PersonSectorsDto("Alice Updated", List.of("IT", "Health"), true);

    service.save(dto, session);

    verify(personRepository).update(eq(123L), argThat(p -> p.getName().equals("Alice Updated")));
    verify(personSectorRepository).delete(123L, Set.of("Finance"));
    verify(personSectorRepository).save(123L, Set.of("Health"));
  }

  @Test
  void save_shouldDoNothing_whenPersonNotFoundButSessionIdPresent() {
    when(session.getAttribute("personId")).thenReturn(123L);
    when(personRepository.findById(123L)).thenReturn(Optional.empty());

    var dto = new PersonSectorsDto("Ghost", List.of("X"), true);

    service.save(dto, session);

    verify(personRepository, never()).update(anyLong(), any());
    verify(personSectorRepository, never()).save(anyLong(), anySet());
  }
}
