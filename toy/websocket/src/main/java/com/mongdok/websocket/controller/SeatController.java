package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.Seat;
import com.mongdok.websocket.repository.SeatRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author: pinest94
 * since: 2021-05-07
 */

@RestController
@RequestMapping("/seats")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class SeatController {

    @Autowired
    SeatRepository seatRepository;

    // 특정 열람실 조회
    @ApiOperation(value = "특정 열람실 정보 조회 🏢")
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> seatInfo(@PathVariable String sessionId) {
        List<Seat> roomList = seatRepository.findAllSeatInfo(sessionId);
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }
}
