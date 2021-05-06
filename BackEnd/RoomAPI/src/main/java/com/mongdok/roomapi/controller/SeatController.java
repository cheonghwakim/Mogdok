package com.mongdok.roomapi.controller;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.service.SeatService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * author: pinest94
 * since: 2021-04-28
 */

@RestController
@RequestMapping("/roomapi/v1/seats")
@Slf4j
@CrossOrigin
public class SeatController {

    @Autowired
    SeatService seatService;

    @ApiOperation(value = "그룹세션 연결 종료요청(열람실을 나갔을 때 📤) - room(session) disconnect")
    @DeleteMapping("")
    public ResponseEntity<?> disconnect(@RequestBody Map<String, String> resource) {
        return seatService.releaseSeat(resource);
    }

    @ApiOperation(value = "좌석에 따른 자원할당요청(좌석에 착석했을 때 📥) - allocate seat")
    @PostMapping("")
    public ResponseEntity<?> allocate(@RequestBody Seat resource) {
        return seatService.allocateSeat(resource);
    }

    @ApiOperation(value = "공부시작 ✍ - start study")
    @PostMapping("/start")
    public ResponseEntity<?> startStudy(@RequestBody Seat resource) {
        return seatService.typeChange(resource, StudyType.START);
    }

    @ApiOperation(value = "휴식시작 🎹 - pause study")
    @PostMapping("/pause")
    public ResponseEntity<?> pauseStudy(@RequestBody Seat resource) {
        return seatService.typeChange(resource, StudyType.PAUSE);
    }
}