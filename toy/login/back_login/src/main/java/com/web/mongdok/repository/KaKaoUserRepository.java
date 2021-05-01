package com.web.mongdok.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.web.mongdok.entity.User;


@Repository
public interface KaKaoUserRepository extends CrudRepository<User, Long> {

	User findById(String id);
	
	List<User> findAll();

	User findByKakaoId(String id);

	User findByEmail(String email);
}