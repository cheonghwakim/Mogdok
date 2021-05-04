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
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;

    /***
     * Redis에서 메시지가 발행되면 해당 메서드가 실행되어 처리한다.
    * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // ChatMessage 객체로 매핑
            RoomMessage roomMessage = objectMapper.readValue(publishMessage, RoomMessage.class);

            // Websocket 구독자에게 채팅 메시지 Send
            messageSendingOperations.convertAndSend("/sub/room/" + roomMessage.getSessionId(), roomMessage);


        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
