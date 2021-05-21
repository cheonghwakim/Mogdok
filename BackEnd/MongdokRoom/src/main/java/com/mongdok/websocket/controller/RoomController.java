package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomElements;
import com.mongdok.websocket.model.StudyRoom;
import com.mongdok.websocket.model.UserInfo;
import com.mongdok.websocket.repository.RoomRepository;
import com.mongdok.websocket.repository.SeatRepository;
import com.mongdok.websocket.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostConstruct
    public void init() {
        roomRepository.createRoom(RoomElements.ROOM_A, RoomElements.ROOM_A_NAME, RoomElements.ROOM_A_SIZE);
        roomRepository.createRoom(RoomElements.ROOM_B, RoomElements.ROOM_B_NAME, RoomElements.ROOM_B_SIZE);
        roomRepository.createRoom(RoomElements.ROOM_C, RoomElements.ROOM_C_NAME, RoomElements.ROOM_C_SIZE);
    }

    // 모든 채팅방 목록 반환
    @ApiOperation(value = "모든 열람실 정보 리스트 조회 🏢")
    @GetMapping("")
    public ResponseEntity<?> getRoomList() {
        List<StudyRoom> roomList = roomRepository.findAllRoom();
        Collections.sort(roomList, Comparator.comparing(StudyRoom::getRoomId));
        roomList.stream().forEach(room -> room.setUserCount(seatRepository.getSeatCount(room.getRoomId())));
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    // 특정 채팅방 조회
    @ApiOperation(value = "특정 열람실 정보 조회 🏢")
    @GetMapping("/{roomId}")
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        StudyRoom room = roomRepository.getRoomById(roomId);
        room.setUserCount(seatRepository.getSeatCount(room.getRoomId()));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<?> isEnterUser(@RequestBody Map<String, String> resources) {
        String token = resources.get("token");

        log.info("[TOKEN] : {}", token);

        if(token == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // 열람실에 접속상태인 경우
        if(roomRepository.isEnterUser(jwtUtil.getUserId(token))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}