package com.rookies4.myspringbootlab.exception.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorObject {
    private String code;     // BOOK_NOT_FOUND
    private String message;  // 예외 메시지
    private int status;      // HTTP Status code
    private String path;     // 요청 경로
    @Builder.Default
    private Instant timestamp = Instant.now();

    public static ErrorObject of(String code, String message, int status, String path) {
        return ErrorObject.builder()
                .code(code)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }
}
