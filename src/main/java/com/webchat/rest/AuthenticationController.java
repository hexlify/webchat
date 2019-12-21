package com.webchat.rest;

import com.webchat.dto.auth.AuthenticationRequestDTO;
import com.webchat.dto.auth.AuthenticationResponseDTO;
import com.webchat.dto.auth.RegisterRequestDTO;
import com.webchat.dto.user.UserDTO;
import com.webchat.model.User;
import com.webchat.rest.errors.exceptions.ConflictException;
import com.webchat.rest.errors.exceptions.UserNotFoundException;
import com.webchat.security.jwt.JwtTokenProvider;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
            UserService userService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/login")
    public AuthenticationResponseDTO login(@RequestBody AuthenticationRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, requestDTO.getPassword()));
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        String token = jwtTokenProvider.createToken(username, user.getRoles());
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setToken(token);

        return response;
    }

    @PostMapping(value = "/register")
    public UserDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        User userByUsername = userService.findByUsername(registerRequestDTO.getUsername());
        User userByEmail = userService.findByEmail(registerRequestDTO.getEmail());

        if (userByUsername != null) {
            throw new ConflictException("Username already in use");
        }

        if (userByEmail != null) {
            throw new ConflictException("Email already in use");
        }

        User user = modelMapper.map(registerRequestDTO, User.class);
        try {
            userService.registerUser(user);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException();
        }

        return modelMapper.map(user, UserDTO.class);
    }
}