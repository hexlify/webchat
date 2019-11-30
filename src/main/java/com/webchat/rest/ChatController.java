package com.webchat.rest;

import com.webchat.rest.contracts.ChatMessageRequest;
import com.webchat.model.ChatMessage;
import com.webchat.repository.ChatMessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    private final ChatMessageRepo chatMessageRepo;

    @Autowired
    public ChatController(ChatMessageRepo chatMessageRepo) {
        this.chatMessageRepo = chatMessageRepo;
    }

    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/public")
    public ChatMessageRequest sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageRequest, chatMessage);
        chatMessageRepo.save(chatMessage);

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
