package com.mongdok.roomapi.repository;

import com.mongdok.roomapi.model.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<StudyLog, Long> {
}
