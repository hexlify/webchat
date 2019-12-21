package com.webchat.rest.errors.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}