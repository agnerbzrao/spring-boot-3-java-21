package br.com.spring.agner.rest_with_spring_boot.mapper.custom;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v2.PersonDTOV2;
import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {
    public PersonDTOV2 convertEntityToDTO(PersonModel personModel) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(personModel.getId());
        dto.setFirstName(personModel.getFirstName());
        dto.setLastName(personModel.getLastName());
        dto.setAddress(personModel.getAddress());
        dto.setGender(personModel.getGender());
        dto.setBirthDay(new Date());

        return dto;
    }

    public PersonModel convertDTOToEntity(PersonDTOV2 dtoV2) {
        PersonModel entity = new PersonModel();
        entity.setId(dtoV2.getId());
        entity.setFirstName(dtoV2.getFirstName());
        entity.setLastName(dtoV2.getLastName());
        entity.setAddress(dtoV2.getAddress());
        entity.setGender(dtoV2.getGender());
        return entity;
    }
}
