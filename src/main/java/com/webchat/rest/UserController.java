package com.webchat.rest;

import com.webchat.dto.UserDTO;
import com.webchat.model.User;
import com.webchat.rest.errors.NotFoundException;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserInfo(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new NotFoundException();
        }

        UserDTO result = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(result);
    }
}
