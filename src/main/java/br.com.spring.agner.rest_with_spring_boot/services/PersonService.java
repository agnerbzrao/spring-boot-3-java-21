package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.controllers.PersonController;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v2.PersonDTOV2;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;

import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseListObjects;
import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

import br.com.spring.agner.rest_with_spring_boot.exception.ResourceObjectIsNullException;
import br.com.spring.agner.rest_with_spring_boot.mapper.custom.PersonMapper;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import br.com.spring.agner.rest_with_spring_boot.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper personMapper;

    public List<PersonDTO> findByAll() {
        logger.info("Find All PersonDTO");

        List<PersonDTO> resultParsedList = parseListObjects(personRepository.findAll(), PersonDTO.class);
         resultParsedList.forEach(this::addHateoasLinks);
        return resultParsedList;

    }

    public PersonDTO findById(Long id) {
        logger.info("Find one PersonDTO");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        PersonDTO resultParse = parseObject(personEntity, PersonDTO.class);
        addHateoasLinks(resultParse);
        return  resultParse;
    }

    public PersonDTO create(PersonDTO personDTO) {
        if(personDTO == null) throw new ResourceObjectIsNullException();

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
        if(personDTO == null) throw new ResourceObjectIsNullException();

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

    private void addHateoasLinks(PersonDTO resultParse) {
        resultParse.add(linkTo(methodOn(PersonController.class).findById(resultParse.getId())).withSelfRel().withType("GET"));
        resultParse.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        resultParse.add(linkTo(methodOn(PersonController.class).create(resultParse)).withRel("create").withType("POST"));
        resultParse.add(linkTo(methodOn(PersonController.class).update(resultParse)).withRel("update").withType("PUT"));
        resultParse.add(linkTo(methodOn(PersonController.class).delete(resultParse.getId())).withRel("delete").withType("DELETE"));
    }
}
