package com.webchat.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {

    private String username;
    private String token;
}
