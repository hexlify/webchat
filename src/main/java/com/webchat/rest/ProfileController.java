package com.webchat.rest;

import com.webchat.dto.UserDTO;
import com.webchat.model.User;
import com.webchat.security.jwt.JwtTokenProvider;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user")
public class ProfileController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileController(UserService userService, JwtTokenProvider jwtTokenProvider,
                             ModelMapper modelMapper) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserInfo(HttpServletRequest request) {
        String username = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));

        User user = userService.findByUsername(username);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(userDTO);
    }
}
