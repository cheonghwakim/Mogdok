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
    private Long limitUserCount;

    public static StudyRoom create(String roomId, String name, Long limitUserCount) {
        StudyRoom room = new StudyRoom();
        room.roomId = roomId;
        room.name = name;
        room.userCount = 0L;
        room.limitUserCount = limitUserCount;
        return room;
    }
}
