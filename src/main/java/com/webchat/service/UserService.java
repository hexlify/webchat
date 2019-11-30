package com.webchat.service;

import com.webchat.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(UUID id);

    void delete(UUID id);
}
