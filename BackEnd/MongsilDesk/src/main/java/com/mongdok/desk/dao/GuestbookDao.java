package com.mongdok.desk.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Guestbook;

@Repository
public interface GuestbookDao extends JpaRepository<Guestbook, String> {

	Optional<Guestbook> findByBoardId(long boardId);

	void deleteByBoardId(long boardId);

}
