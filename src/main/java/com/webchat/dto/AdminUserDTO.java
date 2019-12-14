package com.webchat.dto;

import com.webchat.model.enums.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public class AdminUserDTO {

    private String username;
    private String firstName;
    private String email;
    private String password;
    private UserStatus status;

    private List<RoleDTO> roles;
}