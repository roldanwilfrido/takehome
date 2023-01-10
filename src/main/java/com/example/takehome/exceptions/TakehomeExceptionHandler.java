package com.example.takehome.exceptions;

import com.example.takehome.domain.response.TakehomeError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@ControllerAdvice
@RestController
public class TakehomeExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<TakehomeError> sizeCountryCodesParameterException() {

        String msg = "'countryCodes' parameter must have 1 value at least";
        log.warn("[SIZE EXCEPTION] --> " + msg);
        TakehomeError takehomeError = TakehomeError.builder()
                .time(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(msg + ", please check.")
                .build();
        return new ResponseEntity<>(takehomeError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<TakehomeError> missingParameterException(Exception ex) {

        String msg = ex.getMessage();
        log.warn("[MISSING PARAMETER EXCEPTION] --> " + msg);
        TakehomeError takehomeError = TakehomeError.builder()
                .time(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(msg)
                .build();
        return new ResponseEntity<>(takehomeError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TakehomeServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<TakehomeError> serverException(Exception ex) {

        String msg = "An error occurred during establish connection with " + ex.getMessage() + "";
        log.error("[SERVER ERROR] --> " + msg);
        TakehomeError takehomeError = TakehomeError.builder()
                .time(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(msg + ". Please try later.")
                .build();
        return new ResponseEntity<>(takehomeError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TakehomeClientException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<TakehomeError> codecException(Exception ex) {

        String msg = "A codec exception occurred on " + ex.getMessage() + "";
        log.error("[CLIENT ERROR] --> " + msg);
        TakehomeError takehomeError = TakehomeError.builder()
                .time(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(msg)
                .build();
        return new ResponseEntity<>(takehomeError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TakehomeRateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<TakehomeError> codecException() {

        log.warn("[RATE LIMIT REACHED] --> " +
                "Avoided to receive more requests due to the limit rate per seconds was reached");
        TakehomeError takehomeError = TakehomeError.builder()
                .time(Instant.now())
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .message("Too many requests, please try later.")
                .build();
        return new ResponseEntity<>(takehomeError, HttpStatus.TOO_MANY_REQUESTS);
    }

}
