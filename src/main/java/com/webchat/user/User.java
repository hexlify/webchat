package com.webchat.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String email;

    @CreationTimestamp
    private LocalDateTime registeredTimestamp;

    private boolean isActivated;
    private boolean isBanned;
    private String passwordHash;


    public User(String username, String email) {
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
