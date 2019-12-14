package com.webchat.rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.webchat.dto.ChatRoomDTO;
import com.webchat.dto.ChatRoomRequestDTO;
import com.webchat.dto.Views;
import com.webchat.model.ChatRoom;
import com.webchat.repository.ChatRoomRepository;
import com.webchat.rest.errors.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("room")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChatRoomController(ChatRoomRepository chatRoomRepository, ModelMapper modelMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.OPTIONS})
    @JsonView(Views.IdNameDescription.class)
    public List<ChatRoomDTO> getAll() {
        return chatRoomRepository.findAll().stream()
                .map(x -> modelMapper.map(x, ChatRoomDTO.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{chatRoomId}", method = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ChatRoomDTO get(@PathVariable("chatRoomId") ChatRoom chatRoom) {
        return modelMapper.map(chatRoom, ChatRoomDTO.class);
    }

    @PostMapping("create")
    @JsonView(Views.IdNameDescription.class)
    public ChatRoomDTO create(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {
        ChatRoom newChatRoom = modelMapper.map(chatRoomRequestDTO, ChatRoom.class);
        ChatRoom createdChatRoom = chatRoomRepository.save(newChatRoom);
        
        return modelMapper.map(createdChatRoom, ChatRoomDTO.class);
    }

    @PutMapping("{chatRoomId}")
    @JsonView(Views.IdNameDescription.class)
    public ChatRoomDTO update(@PathVariable("chatRoomId") ChatRoom chatRoom,
                              @RequestBody ChatRoomRequestDTO chatRoomRequest) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(chatRoomRequest, chatRoom);
        return modelMapper.map(chatRoomRepository.save(chatRoom), ChatRoomDTO.class);
    }

    @DeleteMapping("{chatRoomId}")
    public void delete(@PathVariable("chatRoomId") ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new NotFoundException();
        }
        chatRoomRepository.delete(chatRoom);
    }
}


// TODO А нужен ли ResponseEntity??