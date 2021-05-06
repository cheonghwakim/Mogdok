package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.pubsub.RedisPublisher;
import com.mongdok.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
public class MessageController {

    private final RedisPublisher redisPublisher;
    private final RoomRepository roomRepository;

    /***
     * 메시지 발행요청처리
     * @param roomMessage
     */
    @MessageMapping("/room/message")
    public void message(RoomMessage roomMessage) {
        log.info("type : {}", roomMessage.getType());
        log.info("sender : {}", roomMessage.getSender());
        log.info("sessionId : {}", roomMessage.getSessionId());
        log.info("message : {}", roomMessage.getMessage());
        if(roomMessage.getSeatInfo() != null) {
            log.info("seatInfo : {}", roomMessage.getSeatInfo().toString());
        }

        if(RoomMessage.MessageType.ENTER.equals(roomMessage.getType())) {
            roomRepository.enterStudyRoom(roomMessage.getSessionId());
            roomMessage.setMessage(roomMessage.getSender() + "님이 입장하셨습니다.");
        }
        redisPublisher.publish(roomRepository.getTopic(roomMessage.getSessionId()), roomMessage);
    }
}
