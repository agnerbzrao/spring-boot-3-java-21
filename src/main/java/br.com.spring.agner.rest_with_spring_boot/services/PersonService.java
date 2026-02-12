package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import br.com.spring.agner.rest_with_spring_boot.data.dto.v2.PersonDTOV2;
import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;

import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseListObjects;
import static br.com.spring.agner.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

import br.com.spring.agner.rest_with_spring_boot.mapper.custom.PersonMapper;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import br.com.spring.agner.rest_with_spring_boot.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        return parseListObjects(personRepository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Find one PersonDTO");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        return parseObject(personEntity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO personDTO) {
        logger.info("Creating one PersonDTO");
        PersonModel personEntity = parseObject(personDTO, PersonModel.class);

        return parseObject(personRepository.save(personEntity), PersonDTO.class);
    }

    public PersonDTOV2 createV2(PersonDTOV2 personDTOV2) {
        logger.info("Creating one PersonDTOV2");
        PersonModel personEntity = personMapper.convertDTOToEntity(personDTOV2);

        return personMapper.convertEntityToDTO(personRepository.save(personEntity));
    }

    public PersonDTO update(PersonDTO personDTO) {
        logger.info("Updating one PersonDTO");
        PersonModel personEntity = personRepository.findById(personDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        personEntity.setFirstName(personDTO.getFirstName());
        personEntity.setLastName(personDTO.getLastName());
        personEntity.setAddress(personDTO.getAddress());
        personEntity.setGender(personDTO.getGender());
        return parseObject(personRepository.save(personEntity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one Person");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(personEntity);
    }
}
