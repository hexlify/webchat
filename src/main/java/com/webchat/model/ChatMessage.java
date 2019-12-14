package com.webchat.model;

import com.webchat.model.enums.ChatMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "chat_messages")
@Data
@ToString(of = {"content", "type"})
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseEntity {

    private long senderId;
    private long chatRoomId;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private ChatMessageType type;
}