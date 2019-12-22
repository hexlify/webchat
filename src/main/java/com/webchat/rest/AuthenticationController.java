package com.webchat.rest;

import com.webchat.dto.auth.AuthenticationRequestDTO;
import com.webchat.dto.auth.AuthenticationResponseDTO;
import com.webchat.dto.auth.RegisterRequestDTO;
import com.webchat.dto.user.UserDTO;
import com.webchat.model.User;
import com.webchat.model.enums.UserStatus;
import com.webchat.rest.errors.exceptions.ConflictException;
import com.webchat.rest.errors.exceptions.UserNotFoundException;
import com.webchat.security.jwt.JwtTokenProvider;
import com.webchat.service.EmailService;
import com.webchat.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
            UserService userService, EmailService emailService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/login")
    public AuthenticationResponseDTO login(@RequestBody AuthenticationRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (user.getStatus() == UserStatus.WAITING_ACTIVATION) {
            throw new DisabledException("Activate account via email");
        }

        if (user.getStatus() == UserStatus.BANNED) {
            throw new LockedException("Account has been banned");
        }

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, requestDTO.getPassword()));
        String token = jwtTokenProvider.createAuthToken(username, user.getRoles());
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

        userService.registerUser(user);
        String token = jwtTokenProvider.createEmailToken(registerRequestDTO.getUsername());
        emailService.sendVerificationMail(user, token);

        return modelMapper.map(user, UserDTO.class);

        // TODO: по хорошему это надо класть в очередь задач
    }
}