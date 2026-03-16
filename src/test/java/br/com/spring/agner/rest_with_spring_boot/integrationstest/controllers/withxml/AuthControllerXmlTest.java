package br.com.spring.agner.rest_with_spring_boot.integrationstest.controllers.withxml;

import br.com.spring.agner.rest_with_spring_boot.config.TestConfigs;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.AccountCredentialsDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.TokenDTO;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {
    private static TokenDTO tokenDto;
    private static XmlMapper xmlMapper;
    @BeforeAll
    static void setUp() {
        tokenDto = new TokenDTO();
        xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
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

        Assert.assertNotNull(tokenDto.getAccessToken());
        Assert.assertNotNull(tokenDto.getRefreshToken());

    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {
        String content = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("username", tokenDto.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        tokenDto = xmlMapper.readValue(content, TokenDTO.class);

        Assert.assertNotNull(tokenDto.getAccessToken());
        Assert.assertNotNull(tokenDto.getRefreshToken());
    }

}
