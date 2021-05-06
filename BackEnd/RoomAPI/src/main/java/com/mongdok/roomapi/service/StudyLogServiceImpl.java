package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.StudyLog;
import com.mongdok.roomapi.model.Timestamp;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.repository.StudyRepository;
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
 * since: 2021-04-29
 */
@Service
@Slf4j
public class StudyLogServiceImpl implements StudyLogService{

    @Autowired
    StudyRepository studyRepository;

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
        studyRepository.saveAll(studyLogList);
    }
}
