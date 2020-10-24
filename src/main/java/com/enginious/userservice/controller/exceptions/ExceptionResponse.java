package com.enginious.userservice.controller.exceptions;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ExceptionResponse {

    @Setter(AccessLevel.PRIVATE)
    private Date timestamp = new Date();
    private String message;
    private List<String> details;

    @Builder
    public ExceptionResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }
}