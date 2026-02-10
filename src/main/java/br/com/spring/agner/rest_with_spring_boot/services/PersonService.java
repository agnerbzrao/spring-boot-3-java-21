package br.com.spring.agner.rest_with_spring_boot.services;

import br.com.spring.agner.rest_with_spring_boot.exception.ResourceNotFoundException;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import br.com.spring.agner.rest_with_spring_boot.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    public List<PersonModel> findByAll() {
        logger.info("Find All PersonModel");

        return personRepository.findAll();
    }

    public PersonModel findById(Long id) {
        logger.info("Find one PersonModel");
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
    }

    private PersonModel mockPerson(int i) {
        logger.info("Find all PersonModel");
        PersonModel personModel = new PersonModel();
        personModel.setId(counter.incrementAndGet());
        personModel.setFirstName("FirstName" + i);
        personModel.setLastName("LastName" + i);
        personModel.setAddress("Rua Address" + i);
        personModel.setGender("Male");
        return personModel;
    }

    public PersonModel create(PersonModel personModel) {
        logger.info("Creating one PersonModel");
        return personRepository.save(personModel);
    }

    public PersonModel update(PersonModel personModel) {
        logger.info("Updating one PersonModel");
        PersonModel personEntity = personRepository.findById(personModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        personEntity.setFirstName(personEntity.getFirstName());
        personEntity.setLastName(personEntity.getLastName());
        personEntity.setAddress(personEntity.getAddress());
        personEntity.setGender(personEntity.getGender());
        return personRepository.save(personModel);
    }

    public void delete(Long id) {
        logger.info("Deleting one PersonModel");
        PersonModel personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(personEntity);
    }
}
