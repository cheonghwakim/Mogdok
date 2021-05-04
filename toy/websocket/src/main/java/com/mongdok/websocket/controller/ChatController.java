package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RequiredArgsConstructor
@Controller
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    /***
     * 메시지 발행요청처리
     * @param chatMessage
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        log.info("type : {}", chatMessage.getType());
        log.info("sender : {}", chatMessage.getSender());
        log.info("sessionId : {}", chatMessage.getSender());
        log.info("message : {}", chatMessage.getMessage());

        if(ChatMessage.MessageType.JOIN.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getSessionId(), chatMessage);
    }
}
