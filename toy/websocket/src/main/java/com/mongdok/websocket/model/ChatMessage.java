package com.mongdok.websocket.model;

import lombok.Data;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
public class ChatMessage {

    public enum MessageType {
        JOIN, TALK
    }
    
    private MessageType type; // 메시지 타입
    private String sessionId; // 방 번호
    private String sender; // 메시지 보낸 사람
    private String message; // 메시지
}
