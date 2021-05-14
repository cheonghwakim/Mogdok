package com.mongdok.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.request.desk.DeskRequest;
import com.mongdok.desk.service.DeskService;
import com.mongdok.desk.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "내책상,다짐", description = "다짐RU")
@CrossOrigin
@RestController
@RequestMapping("/desk")
public class DeskController {
	
	@Autowired
	private DeskService deskService;
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping
	@ApiOperation(value = "나의 다짐 불러오기")
	public ResponseEntity<? extends BasicResponse> getPromiseByUserId(@RequestHeader ("token") String token){
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);

		return deskService.getPromise(userId);
	}
	
	@PutMapping
	@ApiOperation(value = "나의 다짐 수정")
	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(@RequestBody DeskRequest desk,@RequestHeader ("token") String token){
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);
		return deskService.updatePromiseByUserId(desk,userId);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "나의 책상 모든 것 불러오기(메모, 디데이, 방명록, ..)")
	public ResponseEntity<? extends BasicResponse> getAllInfoDesk(@RequestHeader ("token") String token){
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);
		return deskService.getAllInfoDesk(userId);
	}

}
