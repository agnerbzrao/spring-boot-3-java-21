package br.com.spring.agner.rest_with_spring_boot.repository;

import br.com.spring.agner.rest_with_spring_boot.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookModel, Long> {
}
