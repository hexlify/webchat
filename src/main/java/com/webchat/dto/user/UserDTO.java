package com.webchat.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String username;
    private String firstName;
    private String email;
    private List<RoleDTO> roles;
}
