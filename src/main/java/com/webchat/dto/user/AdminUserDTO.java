package com.webchat.dto.user;

import com.webchat.model.enums.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public class AdminUserDTO {

    private long id;
    private String username;
    private String firstName;
    private String email;
    private UserStatus status;

    private List<RoleDTO> roles;
}