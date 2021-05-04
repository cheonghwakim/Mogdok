package com.mongdok.websocket.controller;

import com.mongdok.websocket.model.ChatRoom;
import com.mongdok.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom create(@RequestBody Map<String, String> resource) {
        return chatService.createRoom(resource.get("sessionId"), resource.get("name"));
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
