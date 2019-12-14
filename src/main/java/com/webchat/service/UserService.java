package com.webchat.service;

import com.webchat.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User registerAdmin(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(long id);

    void delete(long id);

    boolean tryBan(User user);
}
