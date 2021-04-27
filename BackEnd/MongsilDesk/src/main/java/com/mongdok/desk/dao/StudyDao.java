package com.mongdok.desk.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Study;
@Repository
public interface StudyDao extends JpaRepository<Study, String>{

}
