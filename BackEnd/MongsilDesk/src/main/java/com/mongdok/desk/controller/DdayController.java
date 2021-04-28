package com.mongdok.desk.controller;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.dday.DdayRequest;
import com.mongdok.desk.service.DdayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "디데이", description = "디데이CRD")
@CrossOrigin
@RestController
@RequestMapping("/desk/dday")
public class DdayController {
	@Autowired
	DdayService ddayService;
	
	@DeleteMapping("/{ddayId}")
	@Transactional
	@ApiOperation(value = "디데이 삭제")
	public ResponseEntity<? extends BasicResponse> deleteDday(@PathVariable long ddayId){
		return ddayService.deleteDday(ddayId);
	}
	
	@PostMapping
	@ApiOperation(value = "디데이 생성")
	public ResponseEntity<? extends BasicResponse> createDday(DdayRequest ddayRequest){
		return ddayService.createDday(ddayRequest);
	}
	
}
