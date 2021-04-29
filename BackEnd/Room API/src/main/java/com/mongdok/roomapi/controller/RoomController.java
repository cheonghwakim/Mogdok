package com.mongdok.roomapi.controller;

import lombok.extern.slf4j.Slf4j;
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
public class RoomController {

    @PostMapping("disconnect")
    public ResponseEntity<?> disconnect(@RequestBody Map<String, String> resource) {

        String sessionId = resource.get("sessionId");
        String connectionId = resource.get("connectionId");
        String userId = resource.get("userId");
        String userName = resource.get("userName");

        log.info("sessionId : {}", sessionId);
        log.info("connectionId : {}", connectionId);
        log.info("userId : {}", userId);
        log.info("userName : {}", userName);

        return new ResponseEntity<>("resource release", HttpStatus.OK);
    }
}
