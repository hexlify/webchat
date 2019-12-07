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
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO, StompHeaderAccessor stompHeaderAccessor) {
        String sender = stompHeaderAccessor.getUser().getName();
        chatMessageDTO.setSender(sender);
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageDTO, chatMessage);
        chatMessageRepository.save(chatMessage);

        return chatMessageDTO;
    }

//    @MessageMapping("/chat/addUser")
//    @SendTo("/topic/public")
//    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessageDTO, StompHeaderAccessor stompHeaderAccessor) {
//        String sender = stompHeaderAccessor.getUser().getName();
//        chatMessageDTO.setSender(sender);
//
//        return chatMessageDTO;
//    }
}

// TODO сессия создается на вкладку в браузере. Следовательно, сработает по разу на вкладку. Ожидается не то
