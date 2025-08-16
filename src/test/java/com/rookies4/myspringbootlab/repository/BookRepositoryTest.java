package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("prod")   // 운영환경
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    private Book sample(String title, String author, String isbn, int price, LocalDate date) {
        return new Book(title, author, isbn, price, date);
    }

    @Test
    @DisplayName("도서 등록 + 샘플런 출력 (testCreateBook)")
    @Transactional
    @Rollback(false)
    void testCreateBook() {
        String isbn1 = "9788956746425";
        String isbn2 = "9788956746432";

        // 1) 두 ISBN 선삭제 (여러 번 돌려도 안전)
        bookRepository.deleteByIsbn(isbn1);
        bookRepository.deleteByIsbn(isbn2);
        bookRepository.flush(); // 삭제를 즉시 DB에 반영해 유니크 충돌 방지

        // 2) 두 권 저장
        Book b1 = sample("스프링 부트 입문", "홍길동", isbn1, 30000, LocalDate.of(2025, 5, 7));
        Book b2 = sample("JPA 프로그래밍", "박둘리", isbn2, 35000, LocalDate.of(2025, 4, 30));
        bookRepository.saveAll(List.of(b1, b2));
        bookRepository.flush(); // 즉시 반영

        // 3) 확인 및 샘플 포맷 출력
        assertThat(bookRepository.existsByIsbn(isbn1)).isTrue();
        assertThat(bookRepository.existsByIsbn(isbn2)).isTrue();
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트 (testFindByIsbn)")
    @Transactional
    void testFindByIsbn() {
        String isbn = "9788956746432";
        if (!bookRepository.existsByIsbn(isbn)) {
            bookRepository.save(sample("JPA 프로그래밍", "박둘리", isbn, 35000, LocalDate.of(2025,4,30)));
        }
        assertThat(bookRepository.findByIsbn(isbn)).isPresent();
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트 (testFindByAuthor)")
    @Transactional
    void testFindByAuthor() {
        bookRepository.save(sample("JPA 프로그래밍", "박둘리", "9788956746500", 35000, LocalDate.of(2025, 4, 30)));
        bookRepository.save(sample("JPA 심화", "박둘리", "9788956746501", 38000, LocalDate.of(2025, 6, 10)));

        List<Book> list = bookRepository.findByAuthor("박둘리");
        assertThat(list).hasSizeGreaterThanOrEqualTo(2);
        assertThat(list).extracting(Book::getTitle).contains("JPA 프로그래밍", "JPA 심화");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트 (testUpdateBook)")
    @Transactional
    void testUpdateBook() {
        Book saved = bookRepository.save(
                sample("스프링 부트 입문", "홍길동", "9788956746600", 30000, LocalDate.of(2025, 5, 7))
        );

        saved.setPrice(32000);
        Book updated = bookRepository.save(saved);

        assertThat(updated.getPrice()).isEqualTo(32000);
    }

    @Test
    @DisplayName("도서 삭제 테스트 (testDeleteBook)")
    @Transactional
    void testDeleteBook() {
        Book saved = bookRepository.save(
                sample("삭제용 도서", "테스터", "9788956746700", 10000, LocalDate.of(2025, 1, 1))
        );
        Long id = saved.getId();

        bookRepository.deleteById(id);

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
