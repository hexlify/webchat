package com.webchat.rest.errors.exceptions;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
