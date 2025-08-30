package com.rookies4.myspringbootlab.controller.dto;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    //POST
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CreateRequest {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(
                regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)"
        )
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date cannot be in the future")
        private LocalDate publishDate;

        @NotNull(message = "Publisher ID is required")
        private Long publisherId;

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

    //PUT
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateRequest {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(
                regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)"
        )
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date cannot be in the future")
        private LocalDate publishDate;

        @NotNull(message = "Publisher ID is required")
        private Long publisherId;

        @Valid
        private BookDetailDTO detailRequest;
    }

    //PATCH
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PatchRequest {
        @Nullable private String title;
        @Nullable private String author;

        @Nullable
        @Pattern(
                regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)"
        )
        private String isbn;

        @Nullable @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Nullable @Past(message = "Publish date cannot be in the future")
        private LocalDate publishDate;

        @Nullable
        private Long publisherId; // null이면 변경 없음

        @Valid @Nullable
        private BookDetailPatchRequest detailRequest;
    }

    //내부 DTO
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

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailPatchRequest {
        @Nullable private String description;
        @Nullable private String language;
        @Nullable private Integer pageCount;
        @Nullable private String publisher;
        @Nullable private String coverImageUrl;
        @Nullable private String edition;
    }

    //Response
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;
        //출판사 요약 정보
        private PublisherView publisher;

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

        //출판사 정보
        public static Response fromEntityWithPublisher(Book book) {
            Response base = fromEntity(book);
            if (book.getPublisher() != null) {
                base.setPublisher(PublisherView.builder()
                        .id(book.getPublisher().getId())
                        .name(book.getPublisher().getName())
                        .establishedDate(book.getPublisher().getEstablishedDate())
                        .address(book.getPublisher().getAddress())
                        .bookCount(null) //기본 null
                        .build());
            }
            return base;
        }

        //출판사 도서 수
        public static Response fromEntityWithPublisher(Book book, long bookCount) {
            Response base = fromEntity(book);
            if (book.getPublisher() != null) {
                base.setPublisher(PublisherView.builder()
                        .id(book.getPublisher().getId())
                        .name(book.getPublisher().getName())
                        .establishedDate(book.getPublisher().getEstablishedDate())
                        .address(book.getPublisher().getAddress())
                        .bookCount(bookCount)
                        .build());
            }
            return base;
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

    //Book
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SimpleResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;

        public static SimpleResponse fromEntity(Book b) {
            return SimpleResponse.builder()
                    .id(b.getId())
                    .title(b.getTitle())
                    .author(b.getAuthor())
                    .isbn(b.getIsbn())
                    .build();
        }
    }

    //Publisher 요약
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PublisherView {
        private Long id;
        private String name;
        private LocalDate establishedDate;
        private String address;
        @Nullable private Long bookCount;
    }
}
