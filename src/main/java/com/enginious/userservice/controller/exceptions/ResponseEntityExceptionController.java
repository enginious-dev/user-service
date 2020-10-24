package com.enginious.userservice.controller.exceptions;

import com.enginious.userservice.controller.exceptions.ExceptionResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ResponseEntityExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public final ResponseEntity<ExceptionResponse> handleEntityNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message("No entity found")
                .details(Collections.singletonList("No entity found with given id"))
                .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message("Constraint violation")
                .details(Collections.singletonList(ExceptionUtils.getRootCauseMessage(ex)))
                .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message("Validation error")
                .details(ex
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(v -> String.format("%s: %s", v.getField(), v.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
