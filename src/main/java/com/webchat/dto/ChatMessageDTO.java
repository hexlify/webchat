package com.webchat.dto;

import com.webchat.model.enums.ChatMessageType;
import lombok.Data;

@Data
public class ChatMessageDTO {

    private String sender;
    private long chatRoomId;
    private String content;
    private ChatMessageType type;
}
