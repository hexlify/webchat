package com.webchat.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(of = {"id"})
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "creation_timestamp")
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    @Column(name = "modified_timestamp")
    private LocalDateTime lastModifiedTimestamp;
}
