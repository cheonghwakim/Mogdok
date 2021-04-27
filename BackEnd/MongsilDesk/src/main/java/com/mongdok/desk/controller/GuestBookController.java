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
import com.mongdok.desk.model.request.guestbook.GuestBookUpdateRequest;
import com.mongdok.desk.model.request.guestbook.GuestbookCreateRequest;
import com.mongdok.desk.service.GuestBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "방명록", description = "방명록CRUD")
@CrossOrigin
@RestController
@RequestMapping("/desk/guestbook")
public class GuestBookController {
	@Autowired
	GuestBookService guestBookService;
	
	@GetMapping
	@ApiOperation(value = "방명록 불러오기")
	public ResponseEntity<? extends BasicResponse> getGuestBookById(int boardId){
		return guestBookService.getGuestBookById(boardId);
	}
	
	@PostMapping
	@ApiOperation(value = "방명록 생성")
	public ResponseEntity<? extends BasicResponse> createGuestBook(GuestbookCreateRequest request){
		return guestBookService.createGuestBook(request);
	}
	
	@PutMapping
	@ApiOperation(value = "방명록 수정")
	public ResponseEntity<? extends BasicResponse> updateGuestBook(GuestBookUpdateRequest request){
		return guestBookService.updateGuestBook(request);
	}
	
	@DeleteMapping
	@Transactional
	@ApiOperation(value = "방명록 삭제")
	public ResponseEntity<? extends BasicResponse> deleteGuestBook(int boardId){
		return guestBookService.deleteGuestBook(boardId);
	}
}
