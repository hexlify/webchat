package com.webchat.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoomId")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public ChatRoom(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public List<ChatMessage> getChatMessages() {
        return Collections.unmodifiableList(chatMessages);
    }
}
