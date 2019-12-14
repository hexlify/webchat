package com.webchat.model;

import com.webchat.model.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(of = {"username", "email"})
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "senderId")
    private List<ChatMessage> messages;

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}