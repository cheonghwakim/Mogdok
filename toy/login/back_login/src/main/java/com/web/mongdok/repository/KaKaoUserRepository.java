package com.web.mongdok.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.web.mongdok.entity.User;


@Repository
public interface KaKaoUserRepository extends CrudRepository<User, Long> {

	User findByUserId(String id);
	
	List<User> findAll();

	Optional<User> findByKakaoId(String id);

	User findByEmail(String email);
}