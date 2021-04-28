package com.mongdok.desk.service.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.CommonResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.dao.DdayDao;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.Dday;
import com.mongdok.desk.model.request.dday.DdayRequest;
import com.mongdok.desk.model.response.dday.DdayResponse;
import com.mongdok.desk.service.DdayService;

@Service
public class DdayServiceImpl implements DdayService{
	@Autowired
	private DdayDao ddayDao;

	public static final Logger logger = LoggerFactory.getLogger(DdayServiceImpl.class);
	
	//dday 삭제
	@Override
	public ResponseEntity<? extends BasicResponse> deleteDday(long ddayId) {
		
		try {
			ddayDao.deleteByDdayId(ddayId);		
		} catch (Exception e) {
			logger.error("dday삭제 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_DELETE_DDAY));
		}

		return ResponseEntity.ok().body(new CommonResponse<String>("dday 삭제완료"));
	}
	
	//dday 생성
	@Override
	public ResponseEntity<? extends BasicResponse> createDday(DdayRequest ddayRequest) {
		DdayResponse response= new DdayResponse();
		
		try {
			Dday dday=new Dday();
			dday.setDeskId(ddayRequest.getDeskId());
			dday.setTitle(ddayRequest.getTitle());
			dday.setFinishDate(ddayRequest.getFinishDate());
			
			Dday save=ddayDao.save(dday);		
			BeanUtils.copyProperties(save,response);//엔티티-> dto 필드 값 복사
		} catch (Exception e) {
			logger.error("dday생성 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_CREATE_DDAY));
		}

		return ResponseEntity.ok().body(new CommonResponse<DdayResponse>(response));
	}

}
