package com.rookies4.myspringbootlab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "도서를 찾을 수 없습니다."),
    BOOK_ISBN_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 ISBN 입니다."),
    PUBLISHER_NOT_FOUND(HttpStatus.NOT_FOUND, "출판사를 찾을 수 없습니다."),
    PUBLISHER_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 출판사 이름입니다."),
    PUBLISHER_HAS_BOOKS(HttpStatus.BAD_REQUEST, "해당 출판사에 등록된 도서가 있어 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
