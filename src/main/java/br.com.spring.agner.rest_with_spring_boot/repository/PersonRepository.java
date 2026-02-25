package br.com.spring.agner.rest_with_spring_boot.repository;

import br.com.spring.agner.rest_with_spring_boot.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonModel p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);
}
