package com.mongdok.roomapi.controller;

import com.mongdok.roomapi.model.Seat;
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
@RequestMapping("/roomapi/v1/")
@Slf4j
@CrossOrigin
public class SeatController {

    @Autowired
    SeatService seatService;

    @DeleteMapping("seats")
    public ResponseEntity<?> disconnect(@RequestBody Map<String, String> resource) {
        return seatService.releaseSeat(resource);
    }

    @PostMapping("seats")
    public ResponseEntity<?> allocate(@RequestBody Seat resource) {
        return seatService.allocateSeat(resource);
    }
}
