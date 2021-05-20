package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
}

