package com.mongdok.desk.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Study;
@Repository
public interface StudyDao extends JpaRepository<Study, String>{
	
	@Query(value = "select * from study where month(study_time) = month(:date) and year(study_time)= year(:date) and user_id=:userId", nativeQuery = true)
	List<Study> findAllInThisMonth(@Param("date") Date date,@Param("userId") String userId);

}
