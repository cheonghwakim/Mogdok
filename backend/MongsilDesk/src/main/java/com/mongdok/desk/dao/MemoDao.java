package com.mongdok.desk.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mongdok.desk.model.Memo;

@Repository
public interface MemoDao extends JpaRepository<Memo, String> {

	void deleteByMemoId(long memoId);

	Optional<Memo> findByMemoId(long memoId);

}
