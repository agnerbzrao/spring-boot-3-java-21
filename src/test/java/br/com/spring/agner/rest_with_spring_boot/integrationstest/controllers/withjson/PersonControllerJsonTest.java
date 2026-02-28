package br.com.spring.agner.rest_with_spring_boot.integrationstest.controllers.withjson;

import br.com.spring.agner.rest_with_spring_boot.config.TestConfigs;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.WrapperPersonDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personDTO = new PersonDTO();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBaseUri("http://localhost")
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();
        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Borner", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        personDTO.setLastName("Berry Jamon");

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Berry Jamon", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Berry Jamon", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disable() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Berry Jamon", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void delete() throws JsonProcessingException {

        given(specification)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
    @Test
    @Order(6)
    void findAll() throws JsonProcessingException {

        String content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 1,"size",12,"direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapperPersonDTO = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapperPersonDTO.getEmbeddedDTO().getPeople();

        PersonDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Agathe", personOne.getFirstName());
        assertEquals("Elsey", personOne.getLastName());
        assertEquals("41 Anthes Park", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Ailis", personFour.getFirstName());
        assertEquals("Linnock", personFour.getLastName());
        assertEquals("942 Mcguire Plaza", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertTrue(personFour.getEnabled());
    }
    private void mockPerson() {
        personDTO.setFirstName("John");
        personDTO.setLastName("Borner");
        personDTO.setAddress("São Paulo - São Paulo - BRAZIL");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }
}