package com.mongdok.websocket.service;

import com.mongdok.websocket.model.StudyLog;
import com.mongdok.websocket.model.Timestamp;
import com.mongdok.websocket.model.enums.StudyType;
import com.mongdok.websocket.repository.StudyLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * author: pinest94
 * since: 2021-05-09
 */

@Slf4j
@Service
public class StudyLogServiceImpl implements StudyLogService {

    @Autowired
    StudyLogRepository studyLogRepository;

    @Override
    @Transactional
    public void saveLog(String userId, List<Timestamp> logList, LocalDateTime startTime) {

        List<StudyLog> studyLogList = new ArrayList<>();

        // studyLogList 리스트 채우기
        logList.forEach(s -> {
            StudyLog studyLog = StudyLog.builder()
                    .studyType(s.getType())
                    .studyTime(s.getTime())
                    .startTime(startTime)
                    .userId(userId)
                    .build();
            studyLogList.add(studyLog);
            log.info(studyLog.toString());
        });

        // 마지막 기록 저장
        studyLogList.add(StudyLog.builder()
                .studyType(StudyType.END)
                .userId(userId)
                .studyTime(LocalDateTime.now(ZoneOffset.UTC))
                .startTime(startTime)
                .build());

        // DB에 저장
        studyLogRepository.saveAll(studyLogList);
    }
}
