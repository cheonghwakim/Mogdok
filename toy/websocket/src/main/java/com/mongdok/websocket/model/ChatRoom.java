package com.mongdok.websocket.model;

import lombok.Data;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
public class ChatRoom {

    private String sessionId;
    private String name;

    public static ChatRoom create(String sessionId, String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.sessionId = sessionId;
        chatRoom.name = name;
        return chatRoom;
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
