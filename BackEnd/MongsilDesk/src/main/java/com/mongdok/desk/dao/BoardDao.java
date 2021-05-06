package com.mongdok.desk.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Board;
@EnableJpaRepositories
@Repository
public interface BoardDao extends JpaRepository<Board, String> {

	Optional<Board> findByBoardId(long boardId);

	void deleteByBoardId(long boardId);

	Page<Board> findAllByDeskId(Pageable pageable, long deskId);


}
