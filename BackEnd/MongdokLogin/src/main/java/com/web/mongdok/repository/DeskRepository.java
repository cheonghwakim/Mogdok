package com.web.mongdok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.mongdok.entity.Desk;
import com.web.mongdok.entity.User;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {

	Desk findByUser(User userId);

	
}
