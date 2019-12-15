package com.webchat.rest;

import com.webchat.dto.chat.ChatMessageDTO;
import com.webchat.model.ChatMessage;
import com.webchat.repository.ChatMessageRepository;
import com.webchat.security.jwt.JwtUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    private final ModelMapper modelMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public ChatController(ModelMapper modelMapper, ChatMessageRepository chatMessageRepository,
                          SimpMessagingTemplate template) {
        this.modelMapper = modelMapper;
        this.chatMessageRepository = chatMessageRepository;
        this.template = template;
    }

    // без префикса
    @MessageMapping("/sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable long chatRoomId, @Payload ChatMessageDTO chatMessageDTO,
                            @AuthenticationPrincipal JwtUser user) {

        chatMessageDTO.setSender(user.getUsername());
        ChatMessage chatMessage = modelMapper.map(chatMessageDTO, ChatMessage.class);
        chatMessage.setSenderId(user.getId());
        chatMessageRepository.save(chatMessage);

        // с префиксом
        this.template.convertAndSend("/topic/room/" + chatRoomId, chatMessageDTO);
    }
}

// TODO сессия создается на вкладку в браузере. Следовательно, сработает по разу на вкладку. Ожидается не то
