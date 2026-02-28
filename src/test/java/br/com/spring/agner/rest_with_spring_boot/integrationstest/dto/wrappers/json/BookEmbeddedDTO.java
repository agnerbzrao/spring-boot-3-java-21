package br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.wrappers.json;

import br.com.spring.agner.rest_with_spring_boot.integrationstest.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("bookDTOList")
    private List<BookDTO> book;

    public BookEmbeddedDTO(){}

    public List<BookDTO> getBook() {
        return book;
    }

    public void setBook(List<BookDTO> book) {
        this.book = book;
    }
}
