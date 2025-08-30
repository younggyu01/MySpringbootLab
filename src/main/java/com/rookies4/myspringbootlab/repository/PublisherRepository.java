package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Publisher;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    @Query("select p from Publisher p left join fetch p.books where p.id = :id")
    Optional<Publisher> findByIdWithBooks(@Param("id") Long id);

    boolean existsByName(String name);
}
