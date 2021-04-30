package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.StudyLog;
import com.mongdok.roomapi.model.Timestamp;

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
