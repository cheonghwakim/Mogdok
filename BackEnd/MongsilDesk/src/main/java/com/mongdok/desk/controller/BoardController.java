package com.mongdok.desk.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
	BoardService guestBookService;
	
	@GetMapping
	@ApiOperation(value = "방명록 불러오기")
	public ResponseEntity<? extends BasicResponse> getGuestBookById(long boardId){
		return guestBookService.getGuestBookById(boardId);
	}
	
	@PostMapping
	@ApiOperation(value = "방명록 생성")
	public ResponseEntity<? extends BasicResponse> createGuestBook(BoardCreateRequest request){
		return guestBookService.createGuestBook(request);
	}
	
	@PutMapping
	@ApiOperation(value = "방명록 수정")
	public ResponseEntity<? extends BasicResponse> updateGuestBook(BoardUpdateRequest request){
		return guestBookService.updateGuestBook(request);
	}
	
	@DeleteMapping
	@Transactional
	@ApiOperation(value = "방명록 삭제")
	public ResponseEntity<? extends BasicResponse> deleteGuestBook(long boardId){
		return guestBookService.deleteGuestBook(boardId);
	}
}
