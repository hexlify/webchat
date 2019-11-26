package com.webchat.api.contracts;

import com.webchat.domain.chatmessage.ChatMessageType;

import java.util.UUID;

public class ChatMessageRequest {

    private String sender;
    private String content;
    private ChatMessageType type;
    private UUID chatRoomId;

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public ChatMessageType getType() {
        return type;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }
}
