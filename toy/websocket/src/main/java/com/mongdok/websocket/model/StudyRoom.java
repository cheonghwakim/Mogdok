package com.mongdok.websocket.model;

import lombok.Data;

import java.io.Serializable;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
public class StudyRoom implements Serializable {

    private String roomId;
    private String name;
    private Long userCount;

    public static StudyRoom create(String roomId, String name) {
        StudyRoom room = new StudyRoom();
        room.roomId = roomId;
        room.name = name;
        room.userCount = 0L;
        return room;
    }
}
