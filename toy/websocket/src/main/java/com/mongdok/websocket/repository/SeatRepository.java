package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.RoomElements;
import com.mongdok.websocket.model.Seat;
import com.mongdok.websocket.model.SeatInfo;
import com.mongdok.websocket.model.Timestamp;
import com.mongdok.websocket.model.enums.StudyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RoomRepository roomRepository;

    // Redis
    private static final String SEAT_INFO = "SEAT_INFO"; // 열람실의 좌석정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Seat> hashOpsSeatInfo;

    // 해당 열람실의 좌석정보 리스트 조회
    public List<Seat> findAllSeatInfo(String roomId) {
        return hashOpsSeatInfo.values(SEAT_INFO+"_"+roomId);
    }

    // 특정 좌석정보 조회
    public Seat findSeatByUserId(String roomId, String userId) {
        Seat seat =  hashOpsSeatInfo.get(SEAT_INFO+"_"+roomId, userId);
        if(seat != null) {
            log.info("[SEAT 조회] : {}", seat.toString());
        }
        return seat;
    }

    // 좌석정보 저장
    public boolean setSeatInfo(String roomId, String userId, String userName, SeatInfo seatInfo) {
        // TODO: 좌석할당 가능한 지 여부확인 구현(이미 누가 착석했거나 좌석이 꽉찬 경우)
        if(hashOpsSeatInfo.hasKey(SEAT_INFO+"_"+roomId, userId)
                && (hashOpsSeatInfo.size(SEAT_INFO+"_"+roomId) >= RoomElements.ROOM_A_SIZE)) {
            return false;
        }

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
        hashOpsSeatInfo.put(SEAT_INFO+"_"+roomId, userId, newSeat);
        return true;
    }

    // 좌석정보 수정
    public void updateSeatInfo(String roomId, String userId, SeatInfo info) {
        log.info("seatInfo : {}", info.toString());

        if(hashOpsSeatInfo.hasKey(SEAT_INFO+"_"+roomId, userId)) {
            Seat seat = findSeatByUserId(roomId, userId);
            if(seat != null) {
                log.info("seat {}", seat.toString());
            } else {
                log.info("seat null");
            }

            List<Timestamp> timestampList = seat.getTimestampList();
            timestampList.add(Timestamp
                    .builder()
                    .time(LocalDateTime.now(ZoneOffset.UTC))
                    .type(info.getStudyType())
                    .build());
            seat.setStudyType(info.getStudyType());
            hashOpsSeatInfo.put(SEAT_INFO+"_"+roomId, userId, seat);
        }
    }

    // 좌석정보 삭제
    public void removeSeatInfo(String roomId, String userId) {
        log.info("=============== seat info 삭제 ===============");
        hashOpsSeatInfo.delete(SEAT_INFO+"_"+roomId, userId);
    }

    public void removeAll(String roomId) {
        for(String key : hashOpsSeatInfo.keys(SEAT_INFO+"_"+roomId)) {
            log.info(key);
            hashOpsSeatInfo.delete(SEAT_INFO+"_"+roomId, key);
            roomRepository.minusUserCount(roomId);
        }
    }
}