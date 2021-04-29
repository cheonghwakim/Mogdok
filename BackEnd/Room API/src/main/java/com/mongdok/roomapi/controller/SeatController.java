package com.mongdok.roomapi.controller;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("")
    public ResponseEntity<?> disconnect(@RequestBody Map<String, String> resource) {
        return seatService.releaseSeat(resource);
    }

    @PostMapping("")
    public ResponseEntity<?> allocate(@RequestBody Seat resource) {
        return seatService.allocateSeat(resource);
    }

    @PostMapping("/start")
    public ResponseEntity<?> startStudy(@RequestBody Seat resource) {
        return seatService.typeChange(resource, StudyType.START);
    }

    @PostMapping("/pause")
    public ResponseEntity<?> pauseStudy(@RequestBody Seat resource) {
        return seatService.typeChange(resource, StudyType.PAUSE);
    }

}
