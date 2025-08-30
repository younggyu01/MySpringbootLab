package com.rookies4.myspringbootlab.exception.advice;

import com.rookies4.myspringbootlab.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class DefaultExceptionAdvice {

    //tatus + ErrorObject
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorObject body = ErrorObject.of(
                "BUSINESS_ERROR",
                ex.getMessage(),
                ex.getStatus().value(),
                req.getRequestURI()
        );
        if (ex.getStatus().is5xxServerError()) log.error("BusinessException: {}", ex.getMessage());
        else log.warn("BusinessException: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    // ISBN 중복 - 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorObject> handleIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorObject body = ErrorObject.of(
                "DATA_INTEGRITY_VIOLATION",
                rootMessage(ex),
                HttpStatus.CONFLICT.value(),
                req.getRequestURI()
        );
        log.warn("IntegrityViolation: {}", body.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 그 외 - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleOther(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        ErrorObject body = ErrorObject.of(
                "INTERNAL_SERVER_ERROR",
                "Unexpected error: " + ex.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String rootMessage(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null) cur = cur.getCause();
        return cur.getMessage();
    }
}
