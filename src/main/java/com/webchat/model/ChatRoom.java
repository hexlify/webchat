package com.webchat.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@ToString(of = {"name", "description"})
@EqualsAndHashCode(callSuper = true)
public class ChatRoom extends BaseEntity {

    private String name;

    private String description;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoomId", orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    // TODO сообщения данной комнаты удаляются множеством запросов, а не одним. Неэффективно

    public ChatRoom(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public List<ChatMessage> getChatMessages() {
        return Collections.unmodifiableList(chatMessages);
    }
}