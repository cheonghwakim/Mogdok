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

@Api(tags = "내책상", description = "다짐RU")
@CrossOrigin
@RestController
@RequestMapping("/desk")
public class DeskController {
	
	@Autowired
	DeskService DeskService;
	
	@GetMapping
	@ApiOperation(value = "나의 다짐 불러오기")
	public ResponseEntity<? extends BasicResponse> getPromiseByUserId(String userEmail){
		return DeskService.getPromiseByUserEmail(userEmail);
	}
	
	@PutMapping
	@ApiOperation(value = "나의 다짐 수정")
	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest desk){
		return DeskService.updatePromiseByUserId(desk);
	}

}
