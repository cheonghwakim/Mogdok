package com.mongdok.websocket.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongdok.websocket.model.RoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * author: pinest94
 * since: 2021-05-05
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;

    /***
     * Redis에서 메시지가 발행되면 해당 메서드가 실행되어 처리한다.
     * @param message
     */
    public void sendMessage(String message) {

        try {
            // redis에서 발행된 데이터를 받아 deserialize
            RoomMessage roomMessage = objectMapper.readValue(message, RoomMessage.class);

            // Websocket 구독자에게 채팅 메시지 Send
            messageSendingOperations.convertAndSend("/sub/room/" + roomMessage.getRoomId(), roomMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
