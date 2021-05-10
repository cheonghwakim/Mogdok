package com.mongdok.websocket.service;

import com.mongdok.websocket.model.Timestamp;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyLogService {

    /***
     * 사용자의 공부시간 로그기록 저장
     * @param userId
     * @param logList
     * @param startTime
     */
    void saveLog(String userId, List<Timestamp> logList, LocalDateTime startTime);
}
