package com.mongdok.desk.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.CommonResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.dao.DeskDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.model.Desk;
import com.mongdok.desk.model.request.desk.DeskRequest;
import com.mongdok.desk.model.response.DeskResponse;
import com.mongdok.desk.service.DeskService;

@Service
public class DeskServiceImpl implements DeskService {
	@Autowired
	private DeskDao deskdao;
	@Autowired
	private UserDao userdao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	//다짐 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getPromiseByUserEmail(String userEmail) {
		DeskResponse deskResponse = new DeskResponse();
		try {
			Optional<Desk> optional = deskdao.findByUserId(userdao.findUserIdByEmail(userEmail));
			if (optional.isPresent()) {
				deskResponse.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 email"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}
		return ResponseEntity.ok().body(new CommonResponse<DeskResponse>(deskResponse));
	}
	
	//다짐 수정
	@Override
	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest deskRequest) {
		DeskResponse deskResponse = new DeskResponse();
		try {
			Optional<Desk> optional = deskdao.findByUserId(userdao.findUserIdByEmail(deskRequest.getUserEmail()));
			if (optional.isPresent()) {
				Desk desk=optional.get();
				desk.setPromise(deskRequest.getPromise());//다짐 수정
				deskdao.save(desk);
				
				deskResponse.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 email"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}

		return ResponseEntity.ok().body(new CommonResponse<DeskResponse>(deskResponse));
	}
}
