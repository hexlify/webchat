package com.webchat.rest;

import com.webchat.dto.ChatMessageDTO;
import com.webchat.model.ChatMessage;
import com.webchat.repository.ChatMessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageDTO, chatMessage);
        chatMessageRepository.save(chatMessage);

        return chatMessageDTO;
    }

    @MessageMapping("/chat/addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessageDTO,
                                  SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessageDTO.getSender());
        return chatMessageDTO;
    }
}
