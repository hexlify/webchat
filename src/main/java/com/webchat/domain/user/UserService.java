package com.webchat.domain.user;

public interface UserService {
    boolean tryRegister(User user, String password);

    boolean verifyPassword(User user, String password);

    User findByUsername(String username);
}
