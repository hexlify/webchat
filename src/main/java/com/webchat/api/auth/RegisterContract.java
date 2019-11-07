package com.webchat.api.auth;

import lombok.Value;

@Value
public class RegisterContract {
    private String username;
    private String email;
    private String password;
}
