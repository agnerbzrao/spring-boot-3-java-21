package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.controllers.BookController;
import br.com.spring.agner.rest_with_spring_boot.controllers.PersonController;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;
import br.com.spring.agner.rest_with_spring_boot.model.BookModel;
import br.com.spring.agner.rest_with_spring_boot.repository.BookRepository;
import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookDTO> pagedResourcesAssembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Find All BookDTO");

        Page<BookModel> booksModel = bookRepository.findAll(pageable);

        Page<BookDTO> booksWithLink = booksModel.map(book -> {
            BookDTO bookDTO = parseObject(book, BookDTO.class);
            addHateoasLinks(bookDTO);
            return bookDTO;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(BookController.class)
                                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())))
                .withSelfRel();
        return pagedResourcesAssembler.toModel(booksWithLink, findAllLinks);
    }

    public BookDTO findById(Long id) {
        logger.info("Find one BookDTO");

        BookModel bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        BookDTO resultParse = parseObject(bookEntity, BookDTO.class);
        addHateoasLinks(resultParse);

        return resultParse;
    }

    public BookDTO create(BookDTO bookDTO) {
        if (bookDTO == null) throw new ResourceObjectIsNullException();

        logger.info("Creating one BookDTO");

        BookModel bookEntity = parseObject(bookDTO, BookModel.class);
        BookDTO resultParse = parseObject(bookRepository.save(bookEntity), BookDTO.class);

        addHateoasLinks(resultParse);
        return resultParse;
    }

    public BookDTO update(BookDTO bookDTO) {
        if (bookDTO == null) throw new ResourceObjectIsNullException();

        logger.info("Updating one BookDTO");

        BookModel bookEntity = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setLaunchDate(bookDTO.getLaunchDate());
        bookEntity.setPrice(bookDTO.getPrice());
        bookEntity.setTitle(bookDTO.getTitle());

        BookDTO resultParse = parseObject(bookRepository.save(bookEntity), BookDTO.class);
        addHateoasLinks(resultParse);

        return resultParse;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book");

        BookModel bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        bookRepository.delete(bookEntity);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}