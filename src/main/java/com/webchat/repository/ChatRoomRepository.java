package com.webchat.repository;

import com.webchat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}