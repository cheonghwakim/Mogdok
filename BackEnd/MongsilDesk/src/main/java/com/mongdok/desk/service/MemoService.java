package com.mongdok.desk.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.memo.MemoCreateRequest;
import com.mongdok.desk.model.request.memo.MemoUpdateRequest;

public interface MemoService {

	public ResponseEntity<? extends BasicResponse> deleteMemo(long memo);

	public ResponseEntity<? extends BasicResponse> createMemo(MemoCreateRequest memoRequest);

	public ResponseEntity<? extends BasicResponse> updateMemo(List<MemoUpdateRequest> memoRequest);

}
