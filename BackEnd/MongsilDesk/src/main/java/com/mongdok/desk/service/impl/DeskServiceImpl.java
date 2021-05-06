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
import com.mongdok.desk.dao.DeskDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.Dday;
import com.mongdok.desk.model.Desk;
import com.mongdok.desk.model.Board;
import com.mongdok.desk.model.Memo;
import com.mongdok.desk.model.request.desk.DeskRequest;
import com.mongdok.desk.model.response.board.BoardOnlyIdResponse;
import com.mongdok.desk.model.response.dday.DdayResponse;
import com.mongdok.desk.model.response.desk.DeskAllResponse;
import com.mongdok.desk.model.response.desk.DeskResponse;
import com.mongdok.desk.model.response.memo.MemoResponse;
import com.mongdok.desk.service.DeskService;

@Service
public class DeskServiceImpl implements DeskService {
	@Autowired
	private DeskDao deskDao;
	@Autowired
	private UserDao userDao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	// 다짐 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getPromiseByUserEmail(String nickname) {
		DeskResponse response = new DeskResponse();
		try {
			Optional<Desk> optional = deskDao.findByUserId(userDao.findUserIdByUserName(nickname));
			if (optional.isPresent()) {
				response.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 닉네임"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		}
		return ResponseEntity.ok().body(new CommonResponse<DeskResponse>(response));
	}

	// 다짐 수정
	@Override
	public ResponseEntity<? extends BasicResponse> updatePromiseByUserId(DeskRequest deskRequest) {
		DeskResponse response = new DeskResponse();
		try {
			Optional<Desk> optional = deskDao.findByUserId(userDao.findUserIdByUserName(deskRequest.getUserName()));
			if (optional.isPresent()) {
				Desk desk = optional.get();
				desk.setPromise(deskRequest.getPromise());// 다짐 수정
				deskDao.save(desk);

				response.setPromise(optional.get().getPromise());
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 닉네임"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_UPDATE_PROMISE));
		}

		return ResponseEntity.ok().body(new CommonResponse<DeskResponse>(response));
	}

	// 내 책상 모든 것 불러오기(메모, 다짐, 방명록, 디데이)
	@Override
	public ResponseEntity<? extends BasicResponse> getAllInfoDesk(String nickname) {
		DeskAllResponse response = new DeskAllResponse();

		try {
			Optional<Desk> optional = deskDao.findByUserId(userDao.findUserIdByUserName(nickname));
			if (optional.isPresent()) {
				Desk desk = optional.get();
				// 엔티티-> dto 변환
				response.setDeskId(desk.getDeskId());
				response.setPromise(desk.getPromise());

				List<MemoResponse> memolist = new ArrayList<MemoResponse>();

				for (Memo origin : desk.getMemoList()) {
					MemoResponse memo = new MemoResponse();
					BeanUtils.copyProperties(origin, memo);
					memolist.add(memo);
				}

				List<DdayResponse> ddaylist = new ArrayList<DdayResponse>();

				for (Dday origin : desk.getDdayList()) {
					DdayResponse dday = new DdayResponse();
					BeanUtils.copyProperties(origin, dday);
					ddaylist.add(dday);
				}

				List<BoardOnlyIdResponse> boardList = new ArrayList<BoardOnlyIdResponse>();

				for (Board origin : desk.getBoardList()) {
					BoardOnlyIdResponse board = new BoardOnlyIdResponse();
					BeanUtils.copyProperties(origin, board);
					boardList.add(board);
				}
				response.setMemoList(memolist);
				response.setDdayList(ddaylist);
				response.setBoardList(boardList);

			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 닉네임"));
			}
		} catch (Exception e) {
			logger.error("내 책상 조회 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_GET_DESK));
		}
		return ResponseEntity.ok().body(new CommonResponse<DeskAllResponse>(response));
	}
}
