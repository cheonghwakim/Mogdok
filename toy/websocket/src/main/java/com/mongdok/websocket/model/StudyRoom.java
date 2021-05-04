package com.mongdok.websocket.model;

import lombok.Data;

import java.io.Serializable;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
public class StudyRoom implements Serializable {

    private String sessionId;
    private String name;

    public static StudyRoom create(String sessionId, String name) {
        StudyRoom room = new StudyRoom();
        room.sessionId = sessionId;
        room.name = name;
        return room;
    }

//    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
//        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
//            sessions.add(session);
//            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
//        }
//        sendMessage(chatMessage, chatService);
//    }
//
//    public <T> void sendMessage(T message, ChatService chatService) {
//        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
//    }
}
