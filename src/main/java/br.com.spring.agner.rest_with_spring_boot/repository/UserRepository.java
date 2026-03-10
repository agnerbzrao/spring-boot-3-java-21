package br.com.spring.agner.rest_with_spring_boot.repository;

import br.com.spring.agner.rest_with_spring_boot.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("SELECT u FROM UserModel u WHERE u.userName =:userName")
    UserModel findByUsername(@Param("userName") String userName);

}