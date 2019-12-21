package com.webchat.rest.errors;


import com.webchat.rest.errors.exceptions.ApiException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(ApiException ex) {
        ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<Object>(apiError, apiError.getHttpStatus());
    }
}