package com.webchat.dto;

import com.webchat.model.ChatMessageType;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageDTO {

    private String sender;
    private String content;
    private ChatMessageType type;
    private UUID chatRoomId;
}
