package com.web.mongdok.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.web.mongdok.entity.Desk;

@Repository
public interface DeskRepository extends CrudRepository<Desk, Long> {

	
}
