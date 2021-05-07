package com.mongdok.desk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.User;

@Repository
public interface UserDao extends JpaRepository<User, String> {
	
	@Query(value = "SELECT nickname FROM user where id=:userId", nativeQuery = true)
	String findUserNameByUserId(@Param("userId") String userId);
	
	@Query(value = "SELECT id FROM user where nickname=:userName", nativeQuery = true)
	String findUserIdByUserName(@Param("userName") String userName);

}
