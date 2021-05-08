package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.service.RoomService;
import com.mongdok.websocket.util.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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

    private final JWTUtil jwtUtil;

    /***
     * 메시지 발행요청처리
     * @param message
     */
    @MessageMapping("/room/message")
    public void message(RoomMessage message, @Header("token") String token) {

        log.info("type : [{}]", message.getType());
        log.info("seatNo : {}", message.getSeatInfo().getSeatNo());
        log.info("studyType : {}", message.getSeatInfo().getStudyType());
        log.info("token : {}", token);

        //TODO: JWT Token을 이용해서 userName 매핑 구현
        if(token == null) { return; } // token이 없으면 발송하지 않음

        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.get("userId", String.class);
        String userName = claims.get("userName", String.class);

        message.setSender(userName);
        message.setUserId(userId);
        
        roomService.sendMessage(message);
    }
}