package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);
    // BookRepository에 추가(중복 스킵용)
    boolean existsByIsbn(String isbn);
    void deleteByIsbn(String isbn);   // 파생 쿼리, Spring Data JPA가 구현해줌
    List<Book> findByAuthor(String author);
}
