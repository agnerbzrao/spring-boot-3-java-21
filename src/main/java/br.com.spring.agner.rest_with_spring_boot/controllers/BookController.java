package br.com.spring.agner.rest_with_spring_boot.controllers;

import br.com.spring.agner.rest_with_spring_boot.controllers.docs.BookControllerDocs;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookService bookService;

    @Override
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
            @RequestParam(value="page", defaultValue = "1") Integer page,
            @RequestParam(value="size", defaultValue = "12") Integer size,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction shortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(shortDirection, "author"));
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @Override
    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    public BookDTO findById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }

    @Override
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    public BookDTO create(@RequestBody BookDTO bookDTO) {
        return bookService.create(bookDTO);
    }

    @Override
    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    public BookDTO update(@RequestBody BookDTO bookDTO) {
        return bookService.update(bookDTO);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}