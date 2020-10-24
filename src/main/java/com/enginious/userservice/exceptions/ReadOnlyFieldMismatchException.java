package com.enginious.userservice.exceptions;

public class ReadOnlyFieldMismatchException extends RuntimeException {

    public ReadOnlyFieldMismatchException(String field) {
        super(String.format("different value for field [%s] (path vs request)", field));
    }
}
