package com.mongdok.desk.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping("/delete")
	@ApiOperation(value = "디데이 삭제")
	public ResponseEntity<? extends BasicResponse> deleteDday(@RequestBody List<Long> ddayIds){
		return ddayService.deleteDday(ddayIds);
	}
	
	@PostMapping("/update")
	@ApiOperation(value = "디데이 생성&수정")
	public ResponseEntity<? extends BasicResponse> createDday(@RequestBody List<DdayRequest> ddayRequests){
		return ddayService.createDday(ddayRequests);
	}
	
}
