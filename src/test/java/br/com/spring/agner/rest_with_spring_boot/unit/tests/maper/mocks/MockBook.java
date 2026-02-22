package br.com.spring.agner.rest_with_spring_boot.unit.tests.maper.mocks;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.model.BookModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockBook {

    public BookModel mockEntity() {
        return mockEntity(0);
    }

    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<BookModel> mockEntityList() {
        List<BookModel> books = new ArrayList<BookModel>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public BookModel mockEntity(Integer number) {
        BookModel book = new BookModel();
        book.setId(number.longValue());
        book.setAuthor("Author Test " + number);
        book.setTitle("Title Test " + number);

        // Exemplo: datas variando conforme o índice
        book.setLaunchDate(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(number));

        // Exemplo: preço variando conforme o índice
        book.setPrice(new BigDecimal("10.00").add(new BigDecimal(number)));

        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setAuthor("Author Test " + number);
        book.setTitle("Title Test " + number);

        book.setLaunchDate(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(number));
        book.setPrice(new BigDecimal("10.00").add(new BigDecimal(number)));

        return book;
    }
}