package com.webchat;

import com.webchat.user.User;
import com.webchat.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private final UserServiceImpl userService;

    public DataLoader(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestUsers();
    }

    private void createTestUsers() {
        userService.register(new User("test_user1", "testuser1@email.net"), "qwerty");
        userService.register(new User("test_user2", "testuser2@email.net"), "qwerty");
        userService.register(new User("test_user3", "testuser3@email.net"), "qwerty");
    }
}
