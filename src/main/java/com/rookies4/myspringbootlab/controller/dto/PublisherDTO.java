package com.rookies4.myspringbootlab.controller.dto;

import com.rookies4.myspringbootlab.entity.Publisher;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class PublisherDTO {

    @Getter @Setter
    public static class Request {
        private String name;
        private LocalDate establishedDate;
        private String address;
    }

    //모든 출판사 조회
    @Getter @Setter @Builder
    public static class SimpleResponse {
        private Long id;
        private String name;
        private LocalDate establishedDate;
        private String address;
        private Long bookCount;

        public static SimpleResponse fromEntityWithCount(Publisher p, long count) {
            return SimpleResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .establishedDate(p.getEstablishedDate())
                    .address(p.getAddress())
                    .bookCount(count)
                    .build();
        }
    }

    //특정 출판사 조회
    @Getter @Setter @Builder
    public static class Response {
        private Long id;
        private String name;
        private LocalDate establishedDate;
        private String address;
        private Long bookCount;
        private List<BookDTO.SimpleResponse> books;

        public static Response fromEntity(Publisher p, long bookCount, List<BookDTO.SimpleResponse> books) {
            return Response.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .establishedDate(p.getEstablishedDate())
                    .address(p.getAddress())
                    .bookCount(bookCount)
                    .books(books)
                    .build();
        }
    }
}
