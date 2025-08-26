package com.rookies4.myspringbootlab.controller.dto;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    // 생성 요청
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CreateRequest {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;

        public Book toEntity() {
            Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .isbn(isbn)
                    .price(price)
                    .publishDate(publishDate)
                    .build();

            if (detailRequest != null) {
                book.setBookDetail(detailRequest.toEntity());
            }
            return book;
        }
    }

    // 전체 수정(put)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateRequest {
        @NotBlank private String title;
        @NotBlank private String author;
        @NotBlank
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$")
        private String isbn;
        @PositiveOrZero private Integer price;
        @Past private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    // 부분 수정(patch)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PatchRequest {
        @Nullable private String title;
        @Nullable private String author;
        @Nullable
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$")
        private String isbn;
        @Nullable @PositiveOrZero private Integer price;
        @Nullable @Past private LocalDate publishDate;

        @Valid
        @Nullable private BookDetailPatchRequest detailRequest;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public BookDetail toEntity() {
            return BookDetail.builder()
                    .description(description)
                    .language(language)
                    .pageCount(pageCount)
                    .publisher(publisher)
                    .coverImageUrl(coverImageUrl)
                    .edition(edition)
                    .build();
        }
    }

    // BookDetail만 patch
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailPatchRequest {
        @Nullable private String description;
        @Nullable private String language;
        @Nullable private Integer pageCount;
        @Nullable private String publisher;
        @Nullable private String coverImageUrl;
        @Nullable private String edition;
    }

    // 응답
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static Response fromEntity(Book book) {
            BookDetailResponse d = null;
            if (book.getBookDetail() != null) {
                d = BookDetailResponse.builder()
                        .id(book.getBookDetail().getId())
                        .description(book.getBookDetail().getDescription())
                        .language(book.getBookDetail().getLanguage())
                        .pageCount(book.getBookDetail().getPageCount())
                        .publisher(book.getBookDetail().getPublisher())
                        .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                        .edition(book.getBookDetail().getEdition())
                        .build();
            }
            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(d)
                    .build();
        }
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}
