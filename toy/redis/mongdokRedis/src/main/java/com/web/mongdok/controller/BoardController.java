package com.web.mongdok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.entity.Board;
import com.web.mongdok.service.BoardService;

@RestController
public class BoardController {
  
  @Autowired
  BoardService service;
  
  
  @GetMapping()
  public List<Board> boards(String size) {
    List<Board> boards = service.getBoards(size);
    return boards;
  }
  
  @GetMapping("count")
  public int count() {
    return BoardService.getDbCount();
  }

}