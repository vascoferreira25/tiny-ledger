package com.example.tinyledger.config;

import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TinyLedgerExceptionHandler {

    @ExceptionHandler(TinyLedgerInvalidArgumentException.class)
    public ProblemDetail handleInvalidArgument(TinyLedgerInvalidArgumentException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail(ex.getMessage());
        return detail;
    }
}
