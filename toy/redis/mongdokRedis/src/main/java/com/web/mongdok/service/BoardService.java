package com.web.mongdok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.web.mongdok.entity.Board;
import com.web.mongdok.repository.BoardRepository;

@Service
public class BoardService {
  
  @Autowired
  BoardRepository repository;
  
  @Cacheable(key = "#size", value = "getBoards")
  public List<Board> getBoards(String size) {
    return repository.createBySize(size);
  }
  
  public static int getDbCount() {
    return BoardRepository.getDbCount();
  }
}