package com.mongdok.desk.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.desk.DeskRequest;


public interface DeskService {

	public ResponseEntity<? extends BasicResponse> getPromiseByUserEmail(String userId) ;

	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest desk);
	

}
