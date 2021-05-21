package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.service.RoomService;
import com.mongdok.websocket.util.JWTUtil;
import com.sun.istack.Nullable;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
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

    private final JWTUtil jwtUtil;

    /***
     * 메시지 발행요청처리
     * @param message
     */
    @ApiOperation(value = "메시지 Publish ✉")
    @MessageMapping("/room/message")
    public void message(RoomMessage message, @Nullable @Header("token") String token) {

        //TODO: JWT Token을 이용해서 userName 매핑 구현
        if(message.getUserId() == null && token == null) {
            return;
        }

        if(token != null) {
            Claims claims = jwtUtil.getClaims(token);
            String userId = claims.get("userId", String.class);
            String userName = claims.get("userName", String.class);

            message.setSender(userName);
            message.setUserId(userId);
        }

        roomService.sendMessage(message);
    }
}