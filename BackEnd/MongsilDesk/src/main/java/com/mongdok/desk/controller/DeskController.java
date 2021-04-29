package com.mongdok.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.desk.DeskRequest;
import com.mongdok.desk.service.DeskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "내책상,다짐", description = "다짐RU")
@CrossOrigin
@RestController
@RequestMapping("/desk")
public class DeskController {
	
	@Autowired
	DeskService deskService;
	
	@GetMapping
	@ApiOperation(value = "나의 다짐 불러오기")
	public ResponseEntity<? extends BasicResponse> getPromiseByUserId(String nickname){
		return deskService.getPromiseByUserEmail(nickname);
	}
	
	@PutMapping
	@ApiOperation(value = "나의 다짐 수정")
	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest desk){
		return deskService.updatePromiseByUserId(desk);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "나의 책상 모든 것 불러오기(메모, 디데이, 방명록, ..)")
	public ResponseEntity<? extends BasicResponse> getAllInfoDesk(String nickname){
		return deskService.getAllInfoDesk(nickname);
	}

}
