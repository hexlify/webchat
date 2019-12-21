package com.webchat.rest;

import com.webchat.dto.user.AdminUserDTO;
import com.webchat.model.User;
import com.webchat.rest.errors.exceptions.BadRequestException;
import com.webchat.rest.errors.exceptions.UserNotFoundException;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<AdminUserDTO> userDTOs = users.stream()
                .map(x -> modelMapper.map(x, AdminUserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping(value = "/ban/{id}")
    public void banUser(@PathVariable("id") User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        boolean wasBanned = userService.tryBan(user);
        if (!wasBanned) {
            throw new BadRequestException("Can't ban admin");
        }
    }

    @GetMapping(value = "/activate/{id}")
    public void activateUser(@PathVariable("id") User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        userService.activate(user);
    }
}
