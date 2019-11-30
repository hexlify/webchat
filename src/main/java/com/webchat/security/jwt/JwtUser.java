package com.webchat.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class JwtUser implements UserDetails {

    private final UUID id;
    private final String username;
    private final String firstName;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final LocalDateTime lastPasswordResetTimestamp;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(UUID id, String username, String firstName, String email, String password, boolean enabled,
                   LocalDateTime lastPasswordResetTimestamp,
                   Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.lastPasswordResetTimestamp = lastPasswordResetTimestamp;
        this.authorities = authorities;
    }

    @JsonIgnore
    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @JsonIgnore
    public LocalDateTime getLastPasswordResetTimestamp() {
        return lastPasswordResetTimestamp;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
