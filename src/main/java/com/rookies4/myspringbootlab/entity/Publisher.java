package com.rookies4.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Publisher {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    private LocalDate establishedDate;

    @Column(length = 500)
    private String address;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    //편의 메서드
    public void addBook(Book book) {
        books.add(book);
        book.setPublisher(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setPublisher(null);
    }
}
