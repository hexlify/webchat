package com.webchat.rest;


import com.webchat.model.User;
import com.webchat.rest.errors.exceptions.UserNotFoundException;
import com.webchat.security.jwt.JwtAuthenticationException;
import com.webchat.security.jwt.JwtTokenProvider;
import com.webchat.security.jwt.JwtUser;
import com.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/email")
public class EmailConfirmationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public EmailConfirmationController(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @GetMapping(value = "/confirm/{token}")
    public void confirmEmail(@PathVariable String token) {

        if (!jwtTokenProvider.validateEmailToken(token)) {
            throw new JwtAuthenticationException("Invalid token");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String username = ((JwtUser) authentication.getPrincipal()).getUsername();

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        userService.activate(user);
    }
}