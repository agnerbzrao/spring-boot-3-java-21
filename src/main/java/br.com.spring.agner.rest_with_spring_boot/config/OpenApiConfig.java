package br.com.spring.agner.rest_with_spring_boot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("REST API's RESTful Java, Spring Boot and Docker")
                        .version("v1")
                        .description("REST API's RESTful Java, Spring Boot and Docker")
                        .termsOfService("https://agner-spring.com.br")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://agner-spring.com.br")
                        )
                );
    }
}
