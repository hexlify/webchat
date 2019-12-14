package com.webchat.statistics;

import com.webchat.model.ChatMessage;
import com.webchat.model.ChatRoom;
import lombok.Data;

import java.util.List;

@Data
public class UserStats {

    private List<ChatMessage> chatMessages;
    private List<ChatRoom> popularRooms;

    public int getMessagesCount() {
        return chatMessages.size();
    }
}
