package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.SeatInfo;
import com.mongdok.websocket.model.StudyRoom;
import com.mongdok.websocket.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RequiredArgsConstructor
@Repository
public class RoomRepository {

    // Redis
    private static final String STUDY_ROOMS = "STUDY_ROOM"; // 열람실 리스트 저장
    private static final String USER_COUNT = "USER_COUNT"; // 열람실에 입장한 클라이언트 수 저장
    private static final String USER_INFO = "USER_INFO"; // 열람실에 입장한 유저 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, StudyRoom> hashOpsStudyRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsUserInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회
    public List<StudyRoom> findAllRoom() {
        return hashOpsStudyRoom.values(STUDY_ROOMS);
    }

    // 특정 채팅방 조회
    public StudyRoom findRoomById(String id) {
        return hashOpsStudyRoom.get(STUDY_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public StudyRoom createRoom(String sessionId, String name) {
        StudyRoom studyRoom = StudyRoom.create(sessionId, name);
        hashOpsStudyRoom.put(STUDY_ROOMS, studyRoom.getSessionId(), studyRoom);
        return studyRoom;
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String userId) {
        hashOpsUserInfo.put(USER_INFO, userId, sessionId);
    }

    public String getUserEnterSessionId(String userId) {
        return hashOpsUserInfo.get(USER_INFO, userId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String userId) {
        hashOpsUserInfo.delete(USER_INFO, userId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String sessionId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + sessionId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String sessionId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + sessionId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String sessionId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + sessionId)).filter(count -> count > 0).orElse(0L);
    }
}
