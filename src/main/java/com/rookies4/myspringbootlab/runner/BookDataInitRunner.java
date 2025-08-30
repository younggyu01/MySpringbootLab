package com.rookies4.myspringbootlab.runner;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.entity.Publisher;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"default","test"})
@RequiredArgsConstructor
public class BookDataInitRunner implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) return;

        Publisher p1 = publisherRepository.save(Publisher.builder()
                .name("Prentice Hall")
                .establishedDate(LocalDate.of(1913,1,1))
                .address("USA")
                .build());

        Book b1 = Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .price(15000)
                .publishDate(LocalDate.of(2008, 8, 1))
                .publisher(p1)
                .build();

        BookDetail d1 = BookDetail.builder()
                .description("새로운 책 설명")
                .language("Korean")
                .pageCount(464)
                .publisher("Prentice Hall")
                .edition("1st")
                .build();
        b1.setBookDetail(d1);

        bookRepository.save(b1);
    }
}
