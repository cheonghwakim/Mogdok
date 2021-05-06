package com.mongdok.websocket.model;

import lombok.Data;

import java.io.Serializable;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
public class StudyRoom implements Serializable {

    private String sessionId;
    private String name;

    public static StudyRoom create(String sessionId, String name) {
        StudyRoom room = new StudyRoom();
        room.sessionId = sessionId;
        room.name = name;
        return room;
    }
}
