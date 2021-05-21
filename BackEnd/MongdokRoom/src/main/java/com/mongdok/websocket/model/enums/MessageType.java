package com.mongdok.websocket.model.enums;

public enum MessageType {
    ENTER, // 열람실 입장
    QUIT, // 열람실 퇴장
    SEAT_ALLOCATED, // 좌석 착석
    SEAT_STATUS, // 공부상태 변경
    SEAT_ALLOCATE_FAIL, // 좌석 착석 실패
    END // 공부 종료(공부를 종료하고 좌석을 반납하는 것이지 열람실을 퇴장하지 않음)
}
