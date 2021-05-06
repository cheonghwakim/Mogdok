package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.model.SeatElements;
import com.mongdok.roomapi.model.Timestamp;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.repository.SeatRedisRepository;
import com.mongdok.roomapi.utils.MakeString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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

    @Autowired
    StudyLogService studyLogService;

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
            return new ResponseEntity<>(SeatElements.SEAT_ALLOCATED_NOT, HttpStatus.CONFLICT);
        }

        List<Timestamp> timestampList = new ArrayList<>();
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        timestampList.add(new Timestamp(nowTime, StudyType.PAUSE));

        // 좌석할당 가능한 경우
        Seat newSeat = Seat.builder()
                .id(seat.getSessionId()+SeatElements.ID_DELIMETER+seat.getUserId())
                .seatNo(seat.getSeatNo())
                .sessionId(seat.getSessionId())
                .userId(seat.getUserId())
                .userName(seat.getUserName())
                .studyType(StudyType.PAUSE)
                .timestampList(timestampList)
                .allocateTime(nowTime)
                .build();

        seatRedisRepository.save(newSeat);
        return new ResponseEntity<>(nowTime, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> releaseSeat(Map<String, String> resource) {

        String id = MakeString.makeId(resource.get("sessionId"), resource.get("userId"));

        Optional<Seat> optional = seatRedisRepository.findById(id);

        // TODO: 해제해야 할 자원이 있는지 확인 -> `없으면 해제할 자원이 없습니다.` 리턴
        if(!optional.isPresent()) {
            return new ResponseEntity<>(SeatElements.SEAT_RELEASE_NONE, HttpStatus.OK);
        }

        // TODO: 해당 공부기록 데이터베이스에 저장
        Seat releaseSeat = optional.get();
        studyLogService.saveLog(releaseSeat.getUserId(), releaseSeat.getTimestampList(), releaseSeat.getAllocateTime());

        // TODO: redis key-value 삭제
        seatRedisRepository.deleteById(id);

        // TODO: 자원해제 완료 리턴
        return new ResponseEntity<>(SeatElements.SEAT_RELEASE_SUCCESS, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> typeChange(Seat seat, StudyType type) {
        // TODO: 해당 좌석에 대한 데이터가 있는 지 여부 확인
        String id = MakeString.makeId(seat.getSessionId(), seat.getUserId());

        return seatRedisRepository.findById(id)
                .map(s-> {
                    LocalDateTime startTime = LocalDateTime.now(ZoneOffset.UTC);
                    Timestamp timestamp = Timestamp.builder().time(startTime).type(type).build();
                    List<Timestamp> timestampList = s.getTimestampList();
                    timestampList.add(timestamp);
                    s.setTimestampList(timestampList);
                    s.setStudyType(type);

                    seatRedisRepository.save(s);
                    return new ResponseEntity(startTime, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(SeatElements.SEAT_ALLOCATED_NOT, HttpStatus.NO_CONTENT));
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
