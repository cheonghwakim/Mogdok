package com.mongdok.desk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Dday;

@Repository
public interface DdayDao extends JpaRepository<Dday, String> {

	void deleteByDdayId(int ddayId);

}
