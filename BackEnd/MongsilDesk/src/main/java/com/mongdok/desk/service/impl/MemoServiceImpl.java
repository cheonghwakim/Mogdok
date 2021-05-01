package com.mongdok.desk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.CommonResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.dao.MemoDao;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.Memo;
import com.mongdok.desk.model.request.memo.MemoCreateRequest;
import com.mongdok.desk.model.request.memo.MemoUpdateRequest;
import com.mongdok.desk.model.response.memo.MemoResponse;
import com.mongdok.desk.service.MemoService;

@Service
public class MemoServiceImpl implements MemoService {
	@Autowired
	private MemoDao memodao;

	public static final Logger logger = LoggerFactory.getLogger(DdayServiceImpl.class);

	// 메모 삭제
	@Override
	public ResponseEntity<? extends BasicResponse> deleteMemo(long memoId) {

		try {
			memodao.deleteByMemoId(memoId);
		} catch (Exception e) {
			logger.error("memo삭제 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_DELETE_MEMO));
		}
		return ResponseEntity.ok().body(new CommonResponse<String>("memo 삭제완료"));
	}

	// 메모 생성
	@Override
	public ResponseEntity<? extends BasicResponse> createMemo(MemoCreateRequest memoRequest) {
		MemoResponse response = new MemoResponse();
		try {
			Memo memo = new Memo();
			memo.setDeskId(memoRequest.getDeskId());
			memo.setContent(memoRequest.getContent());
			Memo save = memodao.save(memo);// 엔티티-> dto 필드 값 복사

			BeanUtils.copyProperties(save, response);
		} catch (Exception e) {
			logger.error("memo생성 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_CREATE_MEMO));
		}
		return ResponseEntity.ok().body(new CommonResponse<MemoResponse>(response));
	}

	// 메모 생성 및 수정
	@Override
	public ResponseEntity<? extends BasicResponse> updateMemo(List<MemoUpdateRequest> memoRequest) {
		List<MemoResponse> memoResponse =new ArrayList<MemoResponse>();
		
		try {
			for(MemoUpdateRequest request: memoRequest) {
				MemoResponse response =new MemoResponse();
				Memo memo=new Memo();
				BeanUtils.copyProperties(request,memo);
				Memo save=memodao.save(memo);
				
				BeanUtils.copyProperties(save,response);//엔티티-> dto 필드 값 복사
				memoResponse.add(response);
			}
		} catch (Exception e) {
			logger.error("memo수정 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_UPDATE_MEMO));
		}
		return ResponseEntity.ok().body(new CommonResponse<List<MemoResponse>>(memoResponse));
	}
}
