package br.com.spring.agner.rest_with_spring_boot.config;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;

@Configuration
public class HateoasConfig {

    /**
     * Explicit bean definition for PagedResourcesAssembler<PersonDTO> so it can be
     * injected in services.
     * The resolver is provided by Spring Data Web autoconfiguration, so it will
     * be available here as well.
     */
    @Bean
    public PagedResourcesAssembler<PersonDTO> personDtoAssembler() {
        // create resolver explicitly because Spring doesn't expose it as a bean by default
        HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
        return new PagedResourcesAssembler<>(resolver, null);
    }

    @Bean
    public PagedResourcesAssembler<BookDTO> bookDtoAssembler() {
        // create resolver explicitly because Spring doesn't expose it as a bean by default
        HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
        return new PagedResourcesAssembler<>(resolver, null);
    }
}
