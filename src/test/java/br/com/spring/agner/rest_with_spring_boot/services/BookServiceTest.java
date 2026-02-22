package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;
import br.com.spring.agner.rest_with_spring_boot.model.BookModel;
import br.com.spring.agner.rest_with_spring_boot.repository.BookRepository;
import br.com.spring.agner.rest_with_spring_boot.unit.tests.maper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<BookModel> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);

        List<BookDTO> books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        BookDTO bookOne = books.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/book")
                        && link.getType().equals("GET")
                ));

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("POST")
                ));

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("PUT")
                ));

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("DELETE")
                ));

        assertEquals("Author Test 1", bookOne.getAuthor());
        assertEquals("Title Test 1", bookOne.getTitle());
        assertEquals(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(1), bookOne.getLaunchDate());
        assertEquals(new BigDecimal("11.00"), bookOne.getPrice());
    }

    @Test
    void findById() {
        BookModel bookModel = input.mockEntity(1);
        bookModel.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(bookModel));

        BookDTO result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/book")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("POST")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("PUT")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("DELETE")
                ));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(1), result.getLaunchDate());
        assertEquals(new BigDecimal("11.00"), result.getPrice());
    }

    @Test
    void create() {
        BookModel bookModel = input.mockEntity(1);
        bookModel.setId(1L);

        BookDTO bookDTO = input.mockDTO(); // number 0

        when(repository.save(any(BookModel.class))).thenReturn(bookModel);

        BookDTO result = service.create(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/book")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/book")
                        && link.getType().equals("POST")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/book/")
                        && link.getType().equals("PUT")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("DELETE")
                ));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(1), result.getLaunchDate());
        assertEquals(new BigDecimal("11.00"), result.getPrice());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(ResourceObjectIsNullException.class, () -> service.create(null));

        String expectedMessage = "Is is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertNotNull(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        BookModel bookModel = input.mockEntity(1);
        bookModel.setId(1L);

        BookDTO bookDTO = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(bookModel));
        when(repository.save(any(BookModel.class))).thenReturn(bookModel);

        BookDTO result = service.update(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("FindAll")
                        && link.getHref().endsWith("/book")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("POST")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("PUT")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/book/1")
                        && link.getType().equals("DELETE")
                ));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(LocalDateTime.of(2020, 1, 1, 10, 0).plusDays(1), result.getLaunchDate());
        assertEquals(new BigDecimal("11.00"), result.getPrice());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(ResourceObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "Is is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertNotNull(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        BookModel bookModel = input.mockEntity(1);
        bookModel.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(bookModel));

        service.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(BookModel.class));
        verifyNoMoreInteractions(repository);
    }
}