package com.webchat.rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.webchat.rest.contracts.NewChatRoomRequest;
import com.webchat.rest.errors.NotFoundException;
import com.webchat.model.ChatRoom;
import com.webchat.repository.ChatRoomRepository;
import com.webchat.model.Views;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cr")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomController(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }


    @GetMapping
    @JsonView(Views.IdNameDescription.class)
    public List<ChatRoom> getAll() {
        return chatRoomRepository.findAll();
    }

    @GetMapping("{chatRoomId}")
//    @JsonView(Views.FullChatRoom.class)  почему то не отдает список сообщений
    public ChatRoom get(@PathVariable("chatRoomId") ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        return chatRoom;
    }

    @PostMapping
    @JsonView(Views.IdNameDescription.class)
    public ChatRoom create(@RequestBody NewChatRoomRequest newChatRoomRequest) {
        ChatRoom newChatRoom = new ChatRoom(newChatRoomRequest.getName(), newChatRoomRequest.getDescription());
        return chatRoomRepository.save(newChatRoom);
    }

    @PutMapping("{chatRoomId}")
    @JsonView(Views.IdNameDescription.class)
    public ChatRoom update(@PathVariable("chatRoomId") ChatRoom chatRoom,
                           @RequestBody NewChatRoomRequest newChatRoomRequest) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(newChatRoomRequest, chatRoom);
        return chatRoomRepository.save(chatRoom);
    }

    @DeleteMapping("{chatRoomId}")
    public void delete(@PathVariable("chatRoomId") ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        chatRoomRepository.delete(chatRoom);
    }
}
