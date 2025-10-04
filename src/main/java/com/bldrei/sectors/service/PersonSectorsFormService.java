package com.bldrei.sectors.service;

import com.bldrei.sectors.dto.PersonSectorsDto;
import com.bldrei.sectors.entity.PersonEntity;
import com.bldrei.sectors.entity.PersonSectorEntity;
import com.bldrei.sectors.repository.PersonRepository;
import com.bldrei.sectors.repository.PersonSectorRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonSectorsFormService {

  private final PersonSectorRepository personSectorRepository;
  private final PersonRepository personRepository;

  public Optional<PersonSectorsDto> get(HttpSession session) {
    return getPersonIdFromCurrentSession(session)
      .flatMap(personRepository::findById)
      .map(person -> new PersonSectorsDto(
        person.getName(),
        findAllSectorsByPersonId(person.getId()),
        null
      ));
  }

  private List<String> findAllSectorsByPersonId(long personId) {
    return personSectorRepository.findAllSectorsByPersonId(personId)
      .stream()
      .map(PersonSectorEntity::getSectorCode)
      .toList();
  }

  @Transactional
  public void save(PersonSectorsDto personSectorsDto, HttpSession session) {
    getPersonIdFromCurrentSession(session)
      .flatMap(personRepository::findById)
      .ifPresentOrElse(
        currentPersonEntity -> updateExistingPerson(currentPersonEntity.getId(), personSectorsDto),
        () -> {
          long newPersonId = saveNewPerson(personSectorsDto);
          setPersonIdToCurrentSession(newPersonId, session);
        }
      );
  }

  private long saveNewPerson(PersonSectorsDto personSectorsDto) {
    var newPersonEntity = new PersonEntity();
    newPersonEntity.setName(personSectorsDto.name());
    long personId = personRepository.save(newPersonEntity);

    personSectorRepository.save(personId, personSectorsDto.sectors());

    return personId;
  }

  private void updateExistingPerson(long personId, PersonSectorsDto personSectorsDto) {
    var updatedEntity = new PersonEntity();
    updatedEntity.setId(personId);
    updatedEntity.setName(personSectorsDto.name());

    personRepository.update(personId, updatedEntity);

    var currentSectors = personSectorRepository.findAllSectorsByPersonId(personId).stream().map(PersonSectorEntity::getSectorCode).collect(Collectors.toUnmodifiableSet());
    var newSectors = Set.copyOf(personSectorsDto.sectors());

    var sectorsToAdd = difference(newSectors, currentSectors);
    var sectorsToRemove = difference(currentSectors, newSectors);
    personSectorRepository.delete(personId, sectorsToRemove);
    personSectorRepository.save(personId, sectorsToAdd);
  }

  private static <T> Set<T> difference(Set<T> minuend, Set<T> subtrahend) {
    var result = new HashSet<>(minuend);
    result.removeAll(subtrahend);
    return Collections.unmodifiableSet(result);
  }

  private static Optional<Long> getPersonIdFromCurrentSession(HttpSession httpSession) {
    return Optional.ofNullable((Long) httpSession.getAttribute("personId"));
  }

  private static void setPersonIdToCurrentSession(long personId, HttpSession httpSession) {
    httpSession.setAttribute("personId", personId);
  }
}
