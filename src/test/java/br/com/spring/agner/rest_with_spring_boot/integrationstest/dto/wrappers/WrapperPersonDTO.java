package br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers;

import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.json.PersonEmbeddedDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperPersonDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embeddedDTO;


    public WrapperPersonDTO() {}

    public PersonEmbeddedDTO getEmbeddedDTO() {
        return embeddedDTO;
    }

    public void setEmbededDTO(PersonEmbeddedDTO embeddedDTO) {
        this.embeddedDTO = embeddedDTO;
    }
}
