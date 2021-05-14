package com.mongdok.desk.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.request.board.BoardCreateRequest;
import com.mongdok.desk.service.BoardService;
import com.mongdok.desk.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "쪽지", description = "쪽지CRUD")
@CrossOrigin
@RestController
@RequestMapping("/desk/board")
public class BoardController {
	@Autowired
	BoardService boardService;
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping
	@ApiOperation(value = "쪽지 하나 불러오기")
	public ResponseEntity<? extends BasicResponse> getBoardById(long boardId){
		return boardService.getBoardById(boardId);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "유저의 모든 쪽지 불러오기(페이징처리)")
	public ResponseEntity<? extends BasicResponse> getBoardByDeskId(Pageable pageable, long deskId){
		return boardService.getBoardByDeskId(pageable,deskId);
	}
	
	@PostMapping
	@ApiOperation(value = "쪽지 생성")
	public ResponseEntity<? extends BasicResponse> createBoard(@RequestBody BoardCreateRequest request,@RequestHeader ("token") String token){
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);
		
		return boardService.createBoard(request,userId);
	}
	
	@DeleteMapping
	@ApiOperation(value = "쪽지 삭제")
	public ResponseEntity<? extends BasicResponse> deleteBoard(long boardId){
		return boardService.deleteBoard(boardId);
	}
	
	@PutMapping("/read")
	@ApiOperation(value = "방명록 읽음처리")
	public ResponseEntity<? extends BasicResponse> readBoard(long boardId){
		return boardService.readBoard(boardId);
	}
}
