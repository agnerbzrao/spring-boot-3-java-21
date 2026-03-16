package br.com.spring.agner.rest_with_spring_boot.integrationstest.controllers.withxml;

import br.com.spring.agner.rest_with_spring_boot.config.TestConfigs;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.AccountCredentialsDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.TokenDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.xml.PagedModelBook;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static XmlMapper xmlMapper;
    private static BookDTO bookDTO;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        tokenDto = new TokenDTO();
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        bookDTO = new BookDTO();
    }

    @Test
    @Order(0)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials =
                new AccountCredentialsDTO("leandro", "admin123");

        String content = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenDto = xmlMapper.readValue(content, TokenDTO.class);
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader("Accept", MediaType.APPLICATION_XML_VALUE)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBaseUri("http://localhost")
                .setBasePath("/book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter((LogDetail.ALL)))
                .build();

        Assert.assertNotNull(tokenDto.getAccessToken());
        Assert.assertNotNull(tokenDto.getRefreshToken());

    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();
        String xmlBody = xmlMapper.writeValueAsString(bookDTO);
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .body(xmlBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();
        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        bookDTO = createdBook;
        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Steve McConnell", createdBook.getAuthor());
        assertEquals(0, createdBook.getPrice().compareTo(BigDecimal.valueOf(58)));
        assertEquals("Code complete", createdBook.getTitle());
        assertEquals(LocalDateTime.parse("2017-11-07T15:09:01"), createdBook.getLaunchDate().truncatedTo(ChronoUnit.SECONDS));

    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        bookDTO.setAuthor("Berry Jamon");
        String xmlBody = xmlMapper.writeValueAsString(bookDTO);
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .body(xmlBody)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();
        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        bookDTO = createdBook;
        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Berry Jamon", createdBook.getAuthor());
        assertEquals(0, createdBook.getPrice().compareTo(BigDecimal.valueOf(58)));
        assertEquals("Code complete", createdBook.getTitle());
        assertEquals(LocalDateTime.parse("2017-11-07T15:09:01"), createdBook.getLaunchDate().truncatedTo(ChronoUnit.SECONDS));

    }
    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", bookDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        bookDTO = createdBook;
        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Berry Jamon", createdBook.getAuthor());
        assertEquals(0, createdBook.getPrice().compareTo(BigDecimal.valueOf(58)));
        assertEquals("Code complete", createdBook.getTitle());
        assertEquals(LocalDateTime.parse("2017-11-07T15:09:01"), createdBook.getLaunchDate().truncatedTo(ChronoUnit.SECONDS));

    }

    @Test
    @Order(4)
    void delete() throws JsonProcessingException {

        given(specification)
                .pathParam("id", bookDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {

        String content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParam("page", 1,"size",12,"direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelBook wrapper = xmlMapper.readValue(content, PagedModelBook.class);
        List<BookDTO> books = wrapper.getContent();
        BookDTO bookOne = books.getFirst();

        assertNotNull(bookOne.getId());
        assertTrue(bookOne.getId() > 0);

        assertEquals("Steve McConnell", bookOne.getAuthor());
        assertEquals(0, bookOne.getPrice().compareTo(BigDecimal.valueOf(58)));
        assertEquals("Code complete", bookOne.getTitle());
        assertEquals(LocalDateTime.parse("2017-11-07T15:09:01"), bookOne.getLaunchDate().truncatedTo(ChronoUnit.SECONDS));


        BookDTO bookTwo = books.get(2);

        assertNotNull(bookTwo.getId());
        assertTrue(bookTwo.getId() > 0);

        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookTwo.getAuthor());
        assertEquals(0, bookTwo.getPrice().compareTo(BigDecimal.valueOf(54)));
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookTwo.getTitle());
        assertEquals(LocalDateTime.parse("2017-11-07T15:09:01"), bookTwo.getLaunchDate().truncatedTo(ChronoUnit.SECONDS));
    }
    private void mockPerson() {
        bookDTO.setAuthor("Steve McConnell");
        bookDTO.setPrice(BigDecimal.valueOf(58));
        bookDTO.setTitle("Code complete");
        bookDTO.setLaunchDate(LocalDateTime.parse("2017-11-07T15:09:01"));
    }

}
