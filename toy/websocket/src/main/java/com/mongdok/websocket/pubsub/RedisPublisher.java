package com.mongdok.websocket.pubsub;

import com.mongdok.websocket.model.RoomMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * author: pinest94
 * since: 2021-05-05
 */

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    /***
     * 구독자들에게 메시지 발행 서비스 구현
     * @param topic
     * @param message
     */
    public void publish(ChannelTopic topic, RoomMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
