package com.webchat.rest;

import com.webchat.dto.ChatMessageDTO;
import com.webchat.model.ChatMessage;
import com.webchat.repository.ChatMessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public ChatController(ChatMessageRepository chatMessageRepository,
                          SimpMessagingTemplate template) {
        this.chatMessageRepository = chatMessageRepository;
        this.template = template;
    }

    // без префикса
    @MessageMapping("/sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable UUID chatRoomId, @Payload ChatMessageDTO chatMessageDTO,
                                      StompHeaderAccessor stompHeaderAccessor) {

        String sender = stompHeaderAccessor.getUser().getName();
        chatMessageDTO.setSender(sender);
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageDTO, chatMessage);
        chatMessageRepository.save(chatMessage);

        // с префиксом
        this.template.convertAndSend("/topic/room/" + chatRoomId, chatMessageDTO);
    }
}

// TODO сессия создается на вкладку в браузере. Следовательно, сработает по разу на вкладку. Ожидается не то
