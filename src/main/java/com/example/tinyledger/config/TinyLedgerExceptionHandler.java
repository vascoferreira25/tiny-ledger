package com.example.tinyledger.config;

import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import com.example.tinyledger.shared.TinyLedgerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TinyLedgerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(TinyLedgerExceptionHandler.class);

    @ExceptionHandler(TinyLedgerInvalidArgumentException.class)
    public ProblemDetail handleInvalidArgument(TinyLedgerInvalidArgumentException ex) {
        logger.warn("TinyLedgerInvalidArgumentException", ex);
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail(ex.getMessage());
        return detail;
    }

    @ExceptionHandler(TinyLedgerNotFoundException.class)
    public ProblemDetail handleNotFound(TinyLedgerNotFoundException ex) {
        logger.warn("TinyLedgerNotFoundException", ex);
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setDetail(ex.getMessage());
        return detail;
    }
}
