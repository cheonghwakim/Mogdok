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
    private static final String TOKEN_INFO = "TOKEN_INFO"; // 열람실에 연결한 토큰 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, StudyRoom> hashOpsStudyRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsUserInfo;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsTokenInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회
    public List<StudyRoom> findAllRoom() {
        return hashOpsStudyRoom.values(STUDY_ROOMS);
    }

    // 특정 채팅방 조회
    public StudyRoom getRoomById(String id) {
        return hashOpsStudyRoom.get(STUDY_ROOMS, id);
    }

    // 열람실 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public StudyRoom createRoom(String roomId, String name, Long limitUserCount) {
        StudyRoom studyRoom = StudyRoom.create(roomId, name, limitUserCount);
        hashOpsStudyRoom.put(STUDY_ROOMS, studyRoom.getRoomId(), studyRoom);
        return studyRoom;
    }

    // 유저가 입장한 열람실ID와 유저ID 맵핑 정보 저장
    public void setRoomEnterInfo(String userId, String roomId) {
        hashOpsUserInfo.put(USER_INFO, userId, roomId);
    }

    public String getRoomEnterSessionId(String userId) {
        return hashOpsUserInfo.get(USER_INFO, userId);
    }

    // 유저 세션정보와 맵핑된 열람실ID 삭제
    public void removeRoomEnterInfo(String userId) {
        hashOpsUserInfo.delete(USER_INFO, userId);
    }

    // 유저가 열람실에 접속 중인지 확인
    public boolean isEnterUser(String userId) {
        return hashOpsTokenInfo.hasKey(USER_INFO, userId);
    }

   // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }

    // 토큰 저장
    public void setTokenInfo(String sessionId, String token) {
        hashOpsTokenInfo.put(TOKEN_INFO, sessionId, token);
    }

    // 토큰 조회
    public String getTokenBySessionId(String sessionId) {
        return hashOpsTokenInfo.get(TOKEN_INFO, sessionId);
    }

    // 토큰 삭제
    public void removeToken(String sessionId) {
        hashOpsTokenInfo.delete(TOKEN_INFO, sessionId);
    }
}
