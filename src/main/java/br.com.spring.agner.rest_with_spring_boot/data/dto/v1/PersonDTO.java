package br.com.spring.agner.rest_with_spring_boot.data.dto.v1;
import com.fasterxml.jackson.annotation.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Relation(collectionRelation = "people")
@JsonPropertyOrder({"id", "first_name", "last_name", "phoneNumber", "birthDay", "address", "gender", "enabled"})

public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("last_name")
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String phoneNumber;

    private Date birthDay;

    //    @JsonIgnore
    private String address;

    //    @JsonSerialize(using = GenderSerializer.class)
    private String gender;

    private Boolean enabled;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonFormat(pattern = "dd/MM/yyyy")
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(getId(), personDTO.getId()) && Objects.equals(getFirstName(), personDTO.getFirstName()) && Objects.equals(getLastName(), personDTO.getLastName()) && Objects.equals(getPhoneNumber(), personDTO.getPhoneNumber()) && Objects.equals(getBirthDay(), personDTO.getBirthDay()) && Objects.equals(getAddress(), personDTO.getAddress()) && Objects.equals(getGender(), personDTO.getGender()) && Objects.equals(getEnabled(), personDTO.getEnabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getFirstName(), getLastName(), getPhoneNumber(), getBirthDay(), getAddress(), getGender(), getEnabled());
    }
}
