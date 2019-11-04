package com.webchat;

import com.webchat.user.User;
import com.webchat.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestUsers();
    }

    private void createTestUsers() {
        userService.create(new User("TestUser1", "testuser1@email.net", "qwerty"));
        userService.create(new User("TestUser2", "testuser2@email.net", "qwerty"));
        userService.create(new User("TestUser3", "testuser3@email.net", "qwerty"));
    }
}
