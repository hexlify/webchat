package com.webchat.rest.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, String message) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
