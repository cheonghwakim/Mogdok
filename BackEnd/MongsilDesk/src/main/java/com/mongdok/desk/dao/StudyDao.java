package com.mongdok.desk.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Study;

@Repository
public interface StudyDao extends JpaRepository<Study, String> {

	@Query(value = "select * from study where year(start_time)= year(:date) and month(start_time) = month(:date) and user_id=:userId", nativeQuery = true)
	List<Study> findAllInThisMonth(@Param("date") LocalDate date, @Param("userId") String userId);

	@Query(value = "select * from study where date_format(start_time, '%Y-%m-%d') = date_format(:date, '%Y-%m-%d') and user_id=:userId", nativeQuery = true)
	List<Study> findAllInToday(@Param("date") LocalDate to, @Param("userId") String userId);

}
