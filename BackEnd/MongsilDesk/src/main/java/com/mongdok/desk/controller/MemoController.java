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
import com.mongdok.desk.model.request.memo.MemoUpdateRequest;
import com.mongdok.desk.service.MemoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "메모", description = "메모CUD")
@CrossOrigin
@RestController
@RequestMapping("/desk/memo")
public class MemoController {
	@Autowired
	MemoService memoService;
	
	@PostMapping("/delete")
	@ApiOperation(value = "메모 삭제")
	public ResponseEntity<? extends BasicResponse> deleteDday(@RequestBody List<Long> memoIds){
		return memoService.deleteMemo(memoIds);
	}

	@PostMapping("/update")
	@ApiOperation(value = "메모 생성&수정")
	public ResponseEntity<? extends BasicResponse> updateMemo(@RequestBody List<MemoUpdateRequest> memoRequest){
		return memoService.updateMemo(memoRequest);
	}

}
