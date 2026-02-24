package br.com.spring.agner.rest_with_spring_boot.integrationstest.swagger;

import br.com.spring.agner.rest_with_spring_boot.config.TestConfigs;
import br.com.spring.agner.rest_with_spring_boot.integrationstest.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTests extends AbstractIntegrationTest {
    @Test
    void shouldDisplaySwaggerUIPage() {
       String content = given().basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();
       assertTrue(content.contains("Swagger UI"));
    }
}
