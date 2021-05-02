package com.mongdok.desk.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.memo.MemoUpdateRequest;

public interface MemoService {

	public ResponseEntity<? extends BasicResponse> deleteMemo(List<Long> memoIds);

	public ResponseEntity<? extends BasicResponse> updateMemo(List<MemoUpdateRequest> memoRequest);

}
