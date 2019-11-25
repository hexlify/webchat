package com.webchat.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class ChatRoom {

    private UUID id;
    private String name;
    private String description;

    public ChatRoom(String name, String description) {
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
