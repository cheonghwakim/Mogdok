package com.mongdok.desk.service;


import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.dday.DdayRequest;

public interface DdayService {

	public ResponseEntity<? extends BasicResponse> deleteDday(long ddayId);

	public ResponseEntity<? extends BasicResponse> createDday(DdayRequest ddayRequest);
}
