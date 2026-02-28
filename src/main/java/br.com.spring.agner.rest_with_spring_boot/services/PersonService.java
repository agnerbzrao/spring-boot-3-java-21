package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.controllers.PersonController;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v2.PersonDTOV2;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;
import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseObject;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;
import br.com.spring.agner.rest_with_spring_boot.mapper.custom.PersonMapper;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import br.com.spring.agner.rest_with_spring_boot.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Find All PersonDTO");
        Page<PersonModel> people = personRepository.findAll(pageable);

        Page<PersonDTO> peopleWithLink = people.map(personModel -> {
            PersonDTO personDTO = parseObject(personModel, PersonDTO.class);
            addHateoasLinks(personDTO);
            return personDTO;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())))
                .withSelfRel();
       return pagedResourcesAssembler.toModel(peopleWithLink, findAllLinks);

    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Find people by name");
        Page<PersonModel> people = personRepository.findPeopleByName(firstName,pageable);

        Page<PersonDTO> peopleWithLink = people.map(personModel -> {
            PersonDTO personDTO = parseObject(personModel, PersonDTO.class);
            addHateoasLinks(personDTO);
            return personDTO;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())))
                .withSelfRel();
       return pagedResourcesAssembler.toModel(peopleWithLink, findAllLinks);

    }

    public PersonDTO findById(Long id) {
        logger.info("Find one PersonDTO");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        PersonDTO resultParse = parseObject(personEntity, PersonDTO.class);
        addHateoasLinks(resultParse);
        return resultParse;
    }

    public PersonDTO create(PersonDTO personDTO) {
        if (personDTO == null) throw new ResourceObjectIsNullException();

        logger.info("Creating one PersonDTO");
        PersonModel personEntity = parseObject(personDTO, PersonModel.class);
        PersonDTO resultParse = parseObject(personRepository.save(personEntity), PersonDTO.class);
        addHateoasLinks(resultParse);
        return resultParse;
    }

    public PersonDTOV2 createV2(PersonDTOV2 personDTOV2) {
        logger.info("Creating one PersonDTOV2");
        PersonModel personEntity = personMapper.convertDTOToEntity(personDTOV2);

        return personMapper.convertEntityToDTO(personRepository.save(personEntity));
    }

    public PersonDTO update(PersonDTO personDTO) {
        if (personDTO == null) throw new ResourceObjectIsNullException();

        logger.info("Updating one PersonDTO");
        PersonModel personEntity = personRepository.findById(personDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        personEntity.setFirstName(personDTO.getFirstName());
        personEntity.setLastName(personDTO.getLastName());
        personEntity.setAddress(personDTO.getAddress());
        personEntity.setGender(personDTO.getGender());
        PersonDTO resultParse = parseObject(personRepository.save(personEntity), PersonDTO.class);
        addHateoasLinks(resultParse);
        return resultParse;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(personEntity);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one Person");
        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.disablePerson(id);
        PersonModel entity = personRepository.findById(id).get();
        PersonDTO resultParse = parseObject(entity, PersonDTO.class);
        addHateoasLinks(resultParse);
        return resultParse;
    }

    private void addHateoasLinks(PersonDTO resultParse) {
        resultParse.add(linkTo(methodOn(PersonController.class).findById(resultParse.getId())).withSelfRel().withType("GET"));
        resultParse.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        resultParse.add(linkTo(methodOn(PersonController.class).create(resultParse)).withRel("create").withType("POST"));
        resultParse.add(linkTo(methodOn(PersonController.class).update(resultParse)).withRel("update").withType("PUT"));
        resultParse.add(linkTo(methodOn(PersonController.class).disablePerson(resultParse.getId())).withRel("disable").withType("PATCH"));
        resultParse.add(linkTo(methodOn(PersonController.class).delete(resultParse.getId())).withRel("delete").withType("DELETE"));
    }
}
