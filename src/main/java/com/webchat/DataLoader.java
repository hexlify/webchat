package com.webchat;

import com.webchat.domain.user.User;
import com.webchat.domain.user.UserServiceImpl;
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
        userService.tryRegister(new User("test_user1", "testuser1@email.net"), "qwerty");
        userService.tryRegister(new User("test_user2", "testuser2@email.net"), "qwerty");
        userService.tryRegister(new User("test_user3", "testuser3@email.net"), "qwerty");
    }
}
