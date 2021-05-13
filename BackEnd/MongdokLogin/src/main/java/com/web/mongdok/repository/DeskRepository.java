package com.web.mongdok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.mongdok.dto.UserProfileDto;
import com.web.mongdok.entity.Desk;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
	
	Desk findByUserId(String userId);

	@Query(value = "select desk.id deskId, user.category category, desk.promise promise from desk, user where desk.user_id=(select id from user where nickname=:userName) and desk.user_id=user.id", nativeQuery = true)
	UserProfileDto findByUserName(@Param("userName") String userName);
}
