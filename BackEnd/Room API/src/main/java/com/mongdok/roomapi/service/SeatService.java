package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.Seat;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SeatService {

    /***
     * 좌석 착석시 자원 할당
     */
    ResponseEntity<?> allocateSeat(Seat seat);

    /***
     * 좌석 이탈시 자원 해제
     */
    ResponseEntity<?> releaseSeat(Map<String, String> resource);

    /***
     * 공부시작
     */
    ResponseEntity<?> startStudy(Seat seat);

    /***
     * 휴식시작
     */
    ResponseEntity<?> restStudy(Seat seat);

    /***
     * 앉을 수 있는 좌석이 있는지 여부 확인
     */
    boolean isSeatFull(String session);

    /***
     * 해당 좌석에 앉을 수 있는지 여부 확인
     */
    boolean isSeat(String sessionId, int seatNo);
}
