package br.com.spring.agner.rest_with_spring_boot.integrationstest.controllers.withjson;

import br.com.spring.agner.rest_with_spring_boot.config.TestConfigs;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
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

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personDTO = new PersonDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBaseUri("http://localhost")
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Borner", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    void createWithWrongOrigin() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_UNKNOWN)
                .setBaseUri("http://localhost")
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract().body().asString();

        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        personDTO.setFirstName("John");
        personDTO.setLastName("Borner");
        personDTO.setAddress("São Paulo - São Paulo - BRAZIL");
        personDTO.setGender("Male");
    }

    @Test
    void findAll() {
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBaseUri("http://localhost")
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Borner", createdPerson.getLastName());
        assertEquals("São Paulo - São Paulo - BRAZIL", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(4)
    void findByIdWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_UNKNOWN)
                .setBaseUri("http://localhost")
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract().body().asString();
        assertEquals("Invalid CORS request", content);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}