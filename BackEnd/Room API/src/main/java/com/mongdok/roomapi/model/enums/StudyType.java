package com.mongdok.roomapi.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudyType {

    START("공부시작"),
    PAUSE("휴식시작"),
    END("공부종료");

    private String title;
}
