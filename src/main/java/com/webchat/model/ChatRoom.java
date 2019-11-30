package com.webchat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table
@ToString(of = {"name", "description"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdNameDescription.class)
    private UUID id;

    @JsonView(Views.IdNameDescription.class)
    private String name;

    @JsonView(Views.IdNameDescription.class)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    @JsonView(Views.FullChatRoom.class)
    private LocalDateTime creationTimestamp;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_room_id")
    @JsonView(Views.FullChatRoom.class)
    private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

    public ChatRoom(String name, String description) {
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
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

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }
}
