package com.rookies4.myspringbootlab.controller.dto;

import com.rookies4.myspringbootlab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;

public class BookDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;
        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;
        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        private String isbn;
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;
        private LocalDate publishDate;

        public Book toEntity() {
            Book b = new Book();
            b.setTitle(title);
            b.setAuthor(author);
            b.setIsbn(isbn);
            b.setPrice(price);
            b.setPublishDate(publishDate);
            return b;
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookUpdateRequest {
        // 모두 선택적(null이면 미변경)
        private String title;
        private String author;
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;
        private LocalDate publishDate;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse from(Book book) {
            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPrice(),
                    book.getPublishDate()
            );
        }
    }
}