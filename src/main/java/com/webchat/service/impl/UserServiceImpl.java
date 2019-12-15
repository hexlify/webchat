package com.webchat.service.impl;

import com.webchat.model.Role;
import com.webchat.model.User;
import com.webchat.model.enums.UserStatus;
import com.webchat.repository.RoleRepository;
import com.webchat.repository.UserRepository;
import com.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        return registerWithRoles(user, Collections.singletonList(roleUser));
    }

    @Override
    public User registerAdmin(User user) {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        return registerWithRoles(user, Collections.singletonList(roleAdmin));
    }

    private User registerWithRoles(User user, List<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        user.setStatus(UserStatus.ACTIVE);

        User registeredUser = userRepository.save(user);
        log.info("{} was registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.DELETED);
            userRepository.save(user);
        }
    }

    @Override
    public boolean tryBan(User user) {
        if (user == null) {
            return false;
        }

        boolean isAdmin = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(x -> x.equals(ADMIN_ROLE_NAME));

        if (!isAdmin) {
            user.setStatus(UserStatus.BANNED);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void activate(User user) {
        if (user == null) {
            return;
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
