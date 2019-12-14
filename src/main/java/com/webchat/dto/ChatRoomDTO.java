package com.webchat.dto;

import com.fasterxml.jackson.annotation.JsonView;
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

    private List<ChatMessageDTO> chatMessages;
}
