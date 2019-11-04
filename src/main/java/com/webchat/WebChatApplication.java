package com.webchat;

import com.webchat.user.User;
import com.webchat.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WebChatApplication {

    @Autowired
    private final UserService userService;

    public WebChatApplication(UserService userService) {
        this.userService = userService;
    }


    public static void main(String[] args) {

        SpringApplication.run(WebChatApplication.class, args);
    }

    private void createTestUsers() {
        userService.create(new User("TestUser1", "testuser1@email.net", "qwerty"));
        userService.create(new User("TestUser2", "testuser2@email.net", "qwerty"));
        userService.create(new User("TestUser3", "testuser3@email.net", "qwerty"));
    }
}