package com.webchat;

import com.webchat.user.User;
import com.webchat.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class Application {

    @Autowired
    private final UserService userService;

    public Application(UserService userService) {
        this.userService = userService;
    }


    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    private void createTestUsers() {
        userService.register(new User("TestUser1", "testuser1@email.net"), "qwerty");
        userService.register(new User("TestUser2", "testuser2@email.net"), "qwerty");
        userService.register(new User("TestUser3", "testuser3@email.net"), "qwerty");
    }
}

