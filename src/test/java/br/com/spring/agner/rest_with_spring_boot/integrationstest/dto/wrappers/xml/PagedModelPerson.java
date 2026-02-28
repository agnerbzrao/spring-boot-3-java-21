package br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.xml;

import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

public class PagedModelPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<PersonDTO> content;

    public PagedModelPerson() {
    }

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}