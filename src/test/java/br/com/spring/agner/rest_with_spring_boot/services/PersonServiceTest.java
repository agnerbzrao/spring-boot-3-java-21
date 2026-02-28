package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import br.com.spring.agner.rest_with_spring_boot.repository.PersonRepository;
import br.com.spring.agner.rest_with_spring_boot.unit.tests.maper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;
    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled("REASON: Still under development")
    void findByAll() {
        List<PersonModel> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTO> people = new ArrayList<>();
                //service.findByAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        PersonDTO personOne = people.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/api/person/v1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("POST")
                ));
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("PUT")
                ));
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("DELETE")
                ));
        assertEquals("Address Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());
    }

    @Test
    void findById() {
        PersonModel personModel = input.mockEntity(1);
        personModel.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(personModel));
        PersonDTO result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/api/person/v1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("POST")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("PUT")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("DELETE")
                ));
        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create() {
        PersonModel personModel = input.mockEntity(1);
        personModel.setId(1L);

        PersonDTO personDTO = input.mockDTO();

        when(repository.save(any(PersonModel.class))).thenReturn(personModel);

        PersonDTO result = service.create(personDTO);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/api/person/v1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("POST")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("PUT")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("DELETE")
                ));
        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(ResourceObjectIsNullException.class,
                () -> {
                    service.create(null);
                });
        String expectedMessage = "Is is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createV2() {
    }

    @Test
    void update() {
        PersonModel personModel = input.mockEntity(1);
        personModel.setId(1L);

        PersonDTO personDTO = input.mockDTO(1);
        when(repository.findById(1L)).thenReturn(Optional.of(personModel));
        when(repository.save(any(PersonModel.class))).thenReturn(personModel);

        PersonDTO result = service.update(personDTO);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/api/person/v1")
                        && link.getType().equals("GET")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("POST")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("PUT")
                ));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/person/v1/1")
                        && link.getType().equals("DELETE")
                ));
        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(ResourceObjectIsNullException.class,
                () -> {
                    service.update(null);
                });
        String expectedMessage = "Is is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void delete() {
        PersonModel personModel = input.mockEntity(1);
        personModel.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(personModel));
        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(PersonModel.class));
        verifyNoMoreInteractions(repository);
    }
}