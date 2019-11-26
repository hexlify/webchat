package com.webchat.api;


import com.fasterxml.jackson.annotation.JsonView;
import com.webchat.api.contracts.NewChatRoomRequest;
import com.webchat.api.errors.NotFoundException;
import com.webchat.domain.chatroom.ChatRoom;
import com.webchat.domain.chatroom.ChatRoomRepo;
import com.webchat.domain.chatroom.Views;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cr")
public class ChatRoomController {

    private final ChatRoomRepo chatRoomRepo;

    @Autowired
    public ChatRoomController(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }


    @GetMapping
    @JsonView(Views.IdNameDescription.class)
    public List<ChatRoom> getAll() {
        return chatRoomRepo.findAll();
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
        return chatRoomRepo.save(newChatRoom);
    }

    @PutMapping("{chatRoomId}")
    @JsonView(Views.IdNameDescription.class)
    public ChatRoom update(@PathVariable("chatRoomId") ChatRoom chatRoom,
                           @RequestBody NewChatRoomRequest newChatRoomRequest) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(newChatRoomRequest, chatRoom);
        return chatRoomRepo.save(chatRoom);
    }

    @DeleteMapping("{chatRoomId}")
    public void delete(@PathVariable("chatRoomId") ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        chatRoomRepo.delete(chatRoom);
    }
}
