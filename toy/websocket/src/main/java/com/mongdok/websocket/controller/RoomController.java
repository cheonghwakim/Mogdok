package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.RoomElements;
import com.mongdok.websocket.model.StudyRoom;
import com.mongdok.websocket.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @PostConstruct
    public void init() {
        roomRepository.createRoom(RoomElements.SESSION_A, RoomElements.SESSION_A_NAME);
        roomRepository.createRoom(RoomElements.SESSION_B, RoomElements.SESSION_B_NAME);
        roomRepository.createRoom(RoomElements.SESSION_C, RoomElements.SESSION_C_NAME);
    }

    // 모든 채팅방 목록 반환
    @GetMapping("")
    public ResponseEntity<?> getRoomList() {
        List<StudyRoom> chatRoomList = roomRepository.findAllRoom();
        return new ResponseEntity<>(chatRoomList, HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> resource) {
        StudyRoom chatRoom = roomRepository.createRoom(resource.get("sessionId"), resource.get("name"));
        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }

    // 특정 채팅방 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> roomInfo(@PathVariable String sessionId) {
        StudyRoom chatRoom = roomRepository.findRoomById(sessionId);
        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }
}