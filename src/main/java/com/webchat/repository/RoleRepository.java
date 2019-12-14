package com.webchat.repository;

import com.webchat.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
