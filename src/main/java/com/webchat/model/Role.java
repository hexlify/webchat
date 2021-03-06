package com.webchat.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@ToString(of = {"name"})
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    public Role(String name) {
        this.name = name;
    }
}