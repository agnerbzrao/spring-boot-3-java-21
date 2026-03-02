package br.com.spring.agner.rest_with_spring_boot.repository;

import br.com.spring.agner.rest_with_spring_boot.integrationstest.testcontainers.AbstractIntegrationTest;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository personRepository;
    private static PersonModel personModel;

    @BeforeAll
    static void setUp() {
        personModel = new PersonModel();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "firstName")
        );
        personModel = personRepository.
                findPeopleByName("iko", pageable)
                .getContent().get(0);
        assertNotNull(personModel);
        assertNotNull(personModel.getId());
        assertEquals("Nikola", personModel.getFirstName());
        assertEquals("Tesla", personModel.getLastName());
        assertEquals("Male", personModel.getGender());
        assertEquals("Smiljan - Croatia", personModel.getAddress());
        assertTrue(personModel.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {

        Long id = personModel.getId();
        personRepository.disablePerson(id);

        Optional<PersonModel> personResult = personRepository.findById(id);
        personModel = personResult.get();

        assertNotNull(personModel);
        assertNotNull(personModel.getId());
        assertEquals("Nikola", personModel.getFirstName());
        assertEquals("Tesla", personModel.getLastName());
        assertEquals("Male", personModel.getGender());
        assertEquals("Smiljan - Croatia", personModel.getAddress());
        assertFalse(personModel.getEnabled());
    }
}