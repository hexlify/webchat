package com.webchat.domain.chatmessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, UUID> {

}