package com.webchat.user;

public interface UserService {
    void register(User user, String password);

    boolean verifyPassword(User user, String password);

    User findByUsername(String username);
}
