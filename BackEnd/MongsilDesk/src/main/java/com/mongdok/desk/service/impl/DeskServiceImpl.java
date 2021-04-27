package com.mongdok.desk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.CommonResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.dao.DeskDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.model.Dday;
import com.mongdok.desk.model.Desk;
import com.mongdok.desk.model.Guestbook;
import com.mongdok.desk.model.Memo;
import com.mongdok.desk.model.request.desk.DeskRequest;
import com.mongdok.desk.model.response.dday.DdayResponse;
import com.mongdok.desk.model.response.desk.DeskAllResponse;
import com.mongdok.desk.model.response.desk.DeskResponse;
import com.mongdok.desk.model.response.guestbook.GuestBookOnlyIdResponse;
import com.mongdok.desk.model.response.guestbook.GuestBookResponse;
import com.mongdok.desk.model.response.memo.MemoResponse;
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
	public ResponseEntity<? extends BasicResponse> getPromiseByUserEmail(String nickname) {
		DeskResponse deskResponse = new DeskResponse();
		try {
			Optional<Desk> optional = deskdao.findByUserId(userdao.findUserIdByNickname(nickname));
			if (optional.isPresent()) {
				System.out.println(optional.get().toString());
				deskResponse.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 닉네임"));
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
			Optional<Desk> optional = deskdao.findByUserId(userdao.findUserIdByNickname(deskRequest.getNickname()));
			if (optional.isPresent()) {
				Desk desk=optional.get();
				desk.setPromise(deskRequest.getPromise());//다짐 수정
				deskdao.save(desk);
				
				deskResponse.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 닉네임"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}

		return ResponseEntity.ok().body(new CommonResponse<DeskResponse>(deskResponse));
	}
	
	//내 책상 모든 것 불러오기(메모, 다짐, 방명록, 디데이)
	@Override
	public ResponseEntity<? extends BasicResponse> getAllInfoDesk(String nickname) {
		DeskAllResponse response =new DeskAllResponse();
		
		try {
			Optional<Desk> optional = deskdao.findByUserId(userdao.findUserIdByNickname(nickname));
			if (optional.isPresent()) {
				Desk desk=optional.get();
				//엔티티-> dto 변환
				response.setDeskId(desk.getDeskId());
				response.setPromise(desk.getPromise());
				
				List<MemoResponse> memolist=new ArrayList<MemoResponse>();
				
				for(Memo origin : desk.getMemoList()) {
					MemoResponse memo=new MemoResponse();
					BeanUtils.copyProperties(origin,memo);
					memolist.add(memo);
				}
				
				List<DdayResponse> ddaylist=new ArrayList<DdayResponse>();
				
				for(Dday origin : desk.getDdayList()) {
					DdayResponse dday=new DdayResponse();
					BeanUtils.copyProperties(origin,dday);
					ddaylist.add(dday);
				}
				
				List<GuestBookOnlyIdResponse> guestbooklist=new ArrayList<GuestBookOnlyIdResponse>();
				
				for(Guestbook origin : desk.getGuestbookList()) {
					GuestBookOnlyIdResponse guestbook=new GuestBookOnlyIdResponse();
					BeanUtils.copyProperties(origin,guestbook);
					guestbooklist.add(guestbook);
				}
				response.setMemoList(memolist);
				response.setDdayList(ddaylist);
				response.setGuestbookList(guestbooklist);
				
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 닉네임"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}
		return ResponseEntity.ok().body(new CommonResponse<DeskAllResponse>(response));
	}
}
