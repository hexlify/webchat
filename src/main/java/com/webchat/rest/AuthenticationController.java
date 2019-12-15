package com.webchat.rest;

import com.webchat.dto.auth.AuthenticationRequestDTO;
import com.webchat.dto.auth.AuthenticationResponseDTO;
import com.webchat.dto.auth.RegisterRequestDTO;
import com.webchat.dto.user.UserDTO;
import com.webchat.model.User;
import com.webchat.rest.errors.ConflictException;
import com.webchat.security.jwt.JwtTokenProvider;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        try {
            String username = requestDTO.getUsername();
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, requestDTO.getPassword()));
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User %s not found", username));
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());
            AuthenticationResponseDTO response = new AuthenticationResponseDTO();
            response.setToken(token);

            return response;

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid user or password");
        }
    }

    @PostMapping(value = "/register")
    public UserDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        User foundUser = userService.findByUsername(registerRequestDTO.getUsername());
        if (foundUser != null) {
            throw new ConflictException("User already exists");
        }
        User user = modelMapper.map(registerRequestDTO, User.class);
        try {
            userService.register(user);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException();
        }

        return modelMapper.map(user, UserDTO.class);
    }
}