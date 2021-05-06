package com.mongdok.desk.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Board;

@Repository
public interface BoardDao extends JpaRepository<Board, String> {

	Optional<Board> findByBoardId(long boardId);

	void deleteByBoardId(long boardId);

}
