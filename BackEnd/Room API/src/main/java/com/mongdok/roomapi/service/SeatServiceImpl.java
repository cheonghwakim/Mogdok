package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.model.SeatElements;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.repository.SeatRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: pinest94
 * since: 2021-04-29
 */

@Service
@Slf4j
public class SeatServiceImpl implements SeatService {

    @Autowired
    SeatRedisRepository seatRedisRepository;

    @Autowired
    StringRedisTemplate redisTemplate;

    /***
     * 좌석할당 로직구현
     * @param seat
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> allocateSeat(Seat seat) {

        // 좌석할당 가능한 지 여부 확인
        if(!isSeatFull(seat.getSessionId()) && isSeat(seat.getSessionId(), seat.getSeatNo())) {
            return new ResponseEntity<>(SeatElements.NO_SEAT_AVAILABLE, HttpStatus.CONFLICT);
        }

        // 좌석할당 가능한 경우
        Seat newSeat = Seat.builder()
                .id(seat.getSessionId()+SeatElements.ID_DELIMETER+seat.getUserId())
                .seatNo(seat.getSeatNo())
                .sessionId(seat.getSessionId())
                .userId(seat.getUserId())
                .userName(seat.getUserName())
                .studyType(StudyType.PAUSE)
                .build();

        seatRedisRepository.save(newSeat);
        return new ResponseEntity<>(SeatElements.SEAT_ALLOCATED, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> releaseSeat(Map<String, String> resource) {

        // TODO: 해제해야 할 자원이 있는지 확인 -> `없으면 해제할 자원이 없습니다.` 리턴
        // TODO: 해당 공부기록 데이터베이스에 저장
        // TODO: redis key-value 삭제
        // TODO: 자원해제 완료 리턴
        return null;
    }

    @Override
    public ResponseEntity<?> startStudy(Seat seat) {
        return null;
    }

    @Override
    public ResponseEntity<?> restStudy(Seat seat) {
        return null;
    }

    /***
     * 앉을 수 있는 좌석이 있는지 여부 파악
     * @param sessionId
     * @return
     */
    @Override
    public boolean isSeatFull(String sessionId) {

        String key = SeatElements.SEAT_HEADER+sessionId+"*";
        int size = redisTemplate.keys(key).size();
        log.info("{} : {}명", sessionId, size);

        switch (sessionId) {
            case SeatElements.SESSION_A: {
                return size < SeatElements.SESSION_A_SEAT_SIZE ? true : false;
            }
            case SeatElements.SESSION_B: {
                return size < SeatElements.SESSION_B_SEAT_SIZE ? true : false;
            }
            case SeatElements.SESSION_C: {
                return size < SeatElements.SESSION_C_SEAT_SIZE ? true : false;
            }
        }
        return false;
    }

    /***
     * 해당 좌석에 앉을 수 있는 지 여부 확인
     * @param sessionId
     * @param seatNo
     * @return
     */
    @Override
    public boolean isSeat(String sessionId, int seatNo) {
        // Iterator<Seat> seatIterator = redisTemplate.keys(SeatElements.SEAT_HEADER+sessionId+"*");
        return true;
    }
}
