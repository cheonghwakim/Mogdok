package com.mongdok.desk.controller;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.memo.MemoCreateRequest;
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
	
	@DeleteMapping("/{memoId}")
	@Transactional
	@ApiOperation(value = "메모 삭제")
	public ResponseEntity<? extends BasicResponse> deleteDday(@PathVariable("memoId") long memoId){
		return memoService.deleteMemo(memoId);
	}
	@PostMapping
	@ApiOperation(value = "메모 생성")
	public ResponseEntity<? extends BasicResponse> createMemo(MemoCreateRequest memoRequest){
		return memoService.createMemo(memoRequest);
	}
	@PutMapping
	@ApiOperation(value = "메모 생성&수정")
	public ResponseEntity<? extends BasicResponse> updateMemo(List<MemoUpdateRequest> memoRequest){
		return memoService.updateMemo(memoRequest);
	}

}
