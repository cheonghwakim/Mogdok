package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
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

    @Autowired
    private RoomService roomService;

    /***
     * 메시지 발행요청처리
     * @param message
     */
    @MessageMapping("/room/message")
    public void message(RoomMessage message) {

        //TODO: JWT Token을 이용해서 userName 매핑 구현
        log.info("type : [{}]", message.getType());
        log.info("userName : {}", message.getSender());
        log.info("userId : {}", message.getUserId());
        log.info("seatNo : {}", message.getSeatInfo().getSeatNo());
        log.info("studyType : {}", message.getSeatInfo().getStudyType());

        roomService.sendMessage(message);
    }
}
