package com.webchat.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.webchat.model.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDTO {
    
    @JsonView(Views.IdNameDescription.class)
    private long id;

    @JsonView(Views.IdNameDescription.class)
    private String name;

    @JsonView(Views.IdNameDescription.class)
    private String description;

    private List<ChatMessage> chatMessages;
}
