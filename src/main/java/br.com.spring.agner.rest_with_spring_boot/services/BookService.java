package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.controllers.BookController;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.BookDTO;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;
import br.com.spring.agner.rest_with_spring_boot.model.BookModel;
import br.com.spring.agner.rest_with_spring_boot.repository.BookRepository;

import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseListObjects;
import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    BookRepository bookRepository;

    public List<BookDTO> findAll() {
        logger.info("Find All BookDTO");

        List<BookDTO> resultParsedList = parseListObjects(bookRepository.findAll(), BookDTO.class);
        resultParsedList.forEach(this::addHateoasLinks);

        return resultParsedList;
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
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}