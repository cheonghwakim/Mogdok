package com.mongdok.desk.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.board.BoardCreateRequest;
import com.mongdok.desk.model.request.board.BoardUpdateRequest;
import com.mongdok.desk.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "방명록", description = "방명록CRUD")
@CrossOrigin
@RestController
@RequestMapping("/desk/board")
public class BoardController {
	@Autowired
	BoardService boardService;
	
	@GetMapping
	@ApiOperation(value = "방명록 하나 불러오기")
	public ResponseEntity<? extends BasicResponse> getBoardById(long boardId){
		return boardService.getBoardById(boardId);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "유저의 모든 방명록 불러오기(페이징처리)")
	public ResponseEntity<? extends BasicResponse> getBoardByDeskId(Pageable pageable, long deskId){
		return boardService.getBoardByDeskId(pageable,deskId);
	}
	
	@PostMapping
	@ApiOperation(value = "방명록 생성")
	public ResponseEntity<? extends BasicResponse> createBoard(BoardCreateRequest request){
		return boardService.createBoard(request);
	}
	
	@PutMapping
	@ApiOperation(value = "방명록 수정")
	public ResponseEntity<? extends BasicResponse> updateBoard(BoardUpdateRequest request){
		return boardService.updateBoard(request);
	}
	
	@DeleteMapping
	@ApiOperation(value = "방명록 삭제")
	public ResponseEntity<? extends BasicResponse> deleteBoard(long boardId){
		return boardService.deleteBoard(boardId);
	}
	
	@PutMapping("/read")
	@ApiOperation(value = "방명록 읽음처리")
	public ResponseEntity<? extends BasicResponse> readBoard(long boardId){
		return boardService.readBoard(boardId);
	}
}
