package com.mongdok.websocket.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudyType {

    START("START", "공부시작"),
    PAUSE("PAUSE", "휴식시작"),
    END("END","공부종료");

    private String title;
    private String description;
}
