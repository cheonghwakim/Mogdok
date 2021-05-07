package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.Seat;
import com.mongdok.websocket.model.SeatInfo;
import com.mongdok.websocket.model.Timestamp;
import com.mongdok.websocket.model.enums.StudyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class SeatRepository {

    // Redis
    private static final String SEAT_INFO = "SEAT_INFO"; // 열람실의 좌석정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Seat> hashOpsSeatInfo;

    // 해당 열람실의 좌석정보 리스트 조회
    public List<Seat> findAllSeatInfo(String sessionId) {
        return hashOpsSeatInfo.values(SEAT_INFO+"_"+sessionId);
    }

    // 특정 좌석정보 조회
    public Seat findSeatByUserId(String sessionId, String userId) {
        return hashOpsSeatInfo.get(SEAT_INFO+"_"+sessionId, userId);
    }

    // 좌석정보 저장
    public void setSeatInfo(String sessionId, String userId, String userName, SeatInfo seatInfo) {
        // TODO: 좌석할당 가능한 지 여부확인 구현

        List<Timestamp> timestampList = new ArrayList<>();
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        timestampList.add(new Timestamp(nowTime, StudyType.PAUSE));

        Seat newSeat = Seat.builder()
                .seatNo(seatInfo.getSeatNo())
                .userId(userId)
                .userName(userName)
                .studyType(StudyType.PAUSE)
                .timestampList(timestampList)
                .allocateTime(nowTime)
                .build();
        hashOpsSeatInfo.put(SEAT_INFO+"_"+sessionId, userId, newSeat);
    }

    // 좌석정보 수정
    public void updateSeatInfo(String sessionId, String userId, SeatInfo info) {
        if(hashOpsSeatInfo.hasKey(SEAT_INFO+"_"+sessionId, userId)) {
            Seat seat = findSeatByUserId(sessionId, userId);
            List<Timestamp> timestampList = seat.getTimestampList();
            timestampList.add(Timestamp
                    .builder()
                    .time(LocalDateTime.now(ZoneOffset.UTC))
                    .type(info.getStudyType())
                    .build());
        }
    }

    // 좌석정보 삭제
    public void removeSeatInfo(String sessionId, String userId) {
        hashOpsSeatInfo.delete(SEAT_INFO+"_"+sessionId, userId);
    }

    public void removeAll(String sessionId) {
        for(String key : hashOpsSeatInfo.keys(SEAT_INFO+"_"+sessionId)) {
            hashOpsSeatInfo.delete(SEAT_INFO+"_"+sessionId, key);

        }
    }
}
