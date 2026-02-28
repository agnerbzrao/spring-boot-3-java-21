package br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.xml;

import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.BookDTO;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

public class PagedModelBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<BookDTO> content;

    public PagedModelBook() {
    }

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }
}