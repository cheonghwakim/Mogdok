package com.mongdok.websocket.service;

import com.mongdok.websocket.model.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRoomMap.values());
    }

    public ChatRoom findRoomById(String sessionId) {
        return chatRoomMap.get(sessionId);
    }

    public ChatRoom createRoom(String sessionId, String name) {
        ChatRoom chatRoom = ChatRoom.create(sessionId, name);
        chatRoomMap.put(sessionId, chatRoom);
        return chatRoom;
    }

//    public <T> void sendMessage(WebSocketSession session, T message) {
//        try {
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
}
