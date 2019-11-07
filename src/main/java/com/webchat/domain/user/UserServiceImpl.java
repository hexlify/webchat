package com.webchat.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean tryRegister(User user, String password) {
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(user.getPasswordHash(), password);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

