package com.webchat.dto.chat;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDTO {

    @JsonView(ChatRoomViews.IdNameDescription.class)
    private long id;

    @JsonView(ChatRoomViews.IdNameDescription.class)
    private String name;

    @JsonView(ChatRoomViews.IdNameDescription.class)
    private String description;

    private List<ChatMessageDTO> chatMessages;
}
