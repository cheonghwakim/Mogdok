package com.mongdok.desk.service;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.desk.DeskRequest;


public interface DeskService {

	public ResponseEntity<? extends BasicResponse> getPromise(String userId) ;

	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest desk, String userId);

	public ResponseEntity<? extends BasicResponse> getAllInfoDesk(String userId);
	
}
