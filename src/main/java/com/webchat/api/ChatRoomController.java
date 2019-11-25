package com.webchat.api;


import com.webchat.api.contracts.NewChatRoomRequest;
import com.webchat.api.errors.NotFoundException;
import com.webchat.model.ChatRoom;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cr")
public class ChatRoomController {

    private List<ChatRoom> chatRooms = new ArrayList<ChatRoom>() {{
        add(new ChatRoom("Room1", "Sample description1"));
        add(new ChatRoom("Room2", "Sample description2"));
        add(new ChatRoom("Room3", "Sample description3"));
    }};

    @GetMapping
    public List<ChatRoom> getAll() {
        return chatRooms;
    }

    @GetMapping("{chatRoomId}")
    public ChatRoom get(@PathVariable UUID chatRoomId) {
        return chatRooms.stream()
                .filter(x -> x.getId().equals(chatRoomId))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ChatRoom create(@RequestBody NewChatRoomRequest newChatRoomRequest) {
        ChatRoom newChatRoom = new ChatRoom(newChatRoomRequest.getName(), newChatRoomRequest.getDescription());
        chatRooms.add(newChatRoom);

        return newChatRoom;
    }

    @PutMapping("{chatRoomId}")
    public ChatRoom update(@PathVariable UUID chatRoomId, @RequestBody NewChatRoomRequest newChatRoomRequest) {
        ChatRoom chatRoomToUpdate = get(chatRoomId);
        chatRoomToUpdate.setName(newChatRoomRequest.getName());
        chatRoomToUpdate.setDescription(newChatRoomRequest.getDescription());

        return chatRoomToUpdate;
    }

    @DeleteMapping("{chatRoomId}")
    public void delete(@PathVariable UUID chatRoomId) {
        ChatRoom chatRoomToDelete = get(chatRoomId);

        chatRooms.remove(chatRoomToDelete);
    }
}
