package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthor(String author);

    //부분 검색
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);

    //테스트, 로직에서 사용
    void deleteByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    //fetch join
    @Query("""
           select b from Book b
           left join fetch b.bookDetail
           where b.id = :id
           """)
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("""
           select b from Book b
           left join fetch b.bookDetail
           where b.isbn = :isbn
           """)
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    //출판사 상세
    @Query("""
           select b from Book b
           left join fetch b.bookDetail
           left join fetch b.publisher
           where b.id = :id
           """)
    Optional<Book> findByIdWithAllDetails(@Param("id") Long id);

    //Publisher
    List<Book> findByPublisherId(Long publisherId);
    Long countByPublisherId(@Param("publisherId") Long publisherId);
}
