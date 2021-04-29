package com.mongdok.desk.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Desk;
@Repository
public interface DeskDao extends JpaRepository<Desk, String>{

	Optional<Desk> findByUserId(String userId);
}
