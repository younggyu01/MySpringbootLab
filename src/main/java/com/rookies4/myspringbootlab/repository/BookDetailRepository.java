package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.BookDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    Optional<BookDetail> findByBookId(Long bookId);

    List<BookDetail> findByPublisherContainingIgnoreCase(String publisher);

    @Query("""
           select d from BookDetail d
           join fetch d.book
           where d.id = :id
           """)
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);
}
