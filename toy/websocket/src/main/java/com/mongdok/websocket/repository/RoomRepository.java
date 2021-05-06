package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.StudyRoom;
import com.mongdok.websocket.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RequiredArgsConstructor
@Repository
public class RoomRepository {

    // 공부방(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    // 구독처리 서비스
    private final RedisSubscriber redisSubscriber;

    // Redis
    private static final String STUDY_ROOMS = "STUDY_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, StudyRoom> opsHashOperations;

    // 공부방의 메시지를 발행하기 위한 redis topic 정보
    // 서버별로 채팅방에 매치되는 topic정보를 통해 Map에 넣어 sessionId로 찾을 수 있도록 한다.
    private Map<String, ChannelTopic> topicMap;

    @PostConstruct
    private void init() {
        opsHashOperations = redisTemplate.opsForHash();
        topicMap = new HashMap<>();
    }

    public List<StudyRoom> findAllRoom() {
        return opsHashOperations.values(STUDY_ROOMS);
    }

    public StudyRoom findRoomById(String sessionId) {
        return opsHashOperations.get(STUDY_ROOMS, sessionId);
    }

    public StudyRoom createRoom(String sessionId, String name) {

        // TODO: 이미 존재하는 경우 생성하지 말아야 함.

        StudyRoom studyRoom = StudyRoom.create(sessionId, name);
        opsHashOperations.put(STUDY_ROOMS, studyRoom.getSessionId(), studyRoom);
        return studyRoom;
    }

    public void enterStudyRoom(String sessionId) {
        ChannelTopic topic = topicMap.get(sessionId);
        if(topic == null) {
            topic = new ChannelTopic(sessionId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topicMap.put(sessionId, topic);
        }
    }

    public ChannelTopic getTopic(String sessionId) {
        return topicMap.get(sessionId);
    }
}
