package com.rookies4.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private LocalDate publishDate;

    //1:1
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BookDetail bookDetail;

    //1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    // 양방향 연관관계
    public void setBookDetail(BookDetail detail) {
        if (this.bookDetail != null) {
            this.bookDetail.setBook(null);
        }
        this.bookDetail = detail;
        if (detail != null) {
            detail.setBook(this);
        }
    }
}
