package com.webchat.domain.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepo extends JpaRepository<ChatRoom, UUID> {
}
