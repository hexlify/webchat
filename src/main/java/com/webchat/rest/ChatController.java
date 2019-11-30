package com.webchat.rest;

import com.webchat.rest.contracts.ChatMessageRequest;
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
    public ChatMessageRequest sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageRequest, chatMessage);
        chatMessageRepository.save(chatMessage);

        return chatMessageRequest;
    }

    @MessageMapping("/chat/addUser")
    @SendTo("/topic/public")
    public ChatMessageRequest addUser(@Payload ChatMessageRequest chatMessage,
                                      SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
