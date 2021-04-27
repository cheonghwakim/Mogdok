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
import com.mongdok.desk.dao.GuestbookDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.model.Guestbook;
import com.mongdok.desk.model.request.guestbook.GuestBookUpdateRequest;
import com.mongdok.desk.model.request.guestbook.GuestbookCreateRequest;
import com.mongdok.desk.model.response.GuestBookResponse;
import com.mongdok.desk.service.GuestBookService;

@Service
public class GuestBookServiceImpl implements GuestBookService {
	@Autowired
	GuestbookDao guestBookDao;
	@Autowired
	private UserDao userDao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	//방명록 id로 방명록 정보 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getGuestBookById(int boardId) {
		GuestBookResponse response = new GuestBookResponse();

		try {
			Optional<Guestbook> optional = guestBookDao.findByBoardId(boardId);
			if (optional.isPresent()) {
				Guestbook guestbook = optional.get();

				response.setBoardId(guestbook.getBoardId());
				response.setContent(guestbook.getContent());
				response.setWriteDate(guestbook.getWriteDate());
				response.setNickName(userDao.findNickNameByUserId(guestbook.getUserId()));//userid를 nickname으로 바꿔보내기
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 불러오기 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}

		return ResponseEntity.ok().body(new CommonResponse<GuestBookResponse>(response));
	}

	// 방명록 작성
	@Override
	public ResponseEntity<? extends BasicResponse> createGuestBook(GuestbookCreateRequest request) {
		GuestBookResponse response = new GuestBookResponse();

		try {
			Guestbook guestbook = new Guestbook();
			guestbook.setContent(request.getContent());
			guestbook.setDeskId(request.getDeskId());
			guestbook.setUserId(userDao.findUserIdByNickname(request.getNickName()));//nickname을 userid 찾아옴
			guestBookDao.save(guestbook);
			
			response.setBoardId(guestbook.getBoardId());
			response.setContent(guestbook.getContent());
			response.setWriteDate(guestbook.getWriteDate());
			response.setNickName(request.getNickName());

		} catch (Exception e) {
			logger.error("방명록 작성 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}
		return ResponseEntity.ok().body(new CommonResponse<GuestBookResponse>(response));
	}

	//방명록 업데이트
	@Override
	public ResponseEntity<? extends BasicResponse> updateGuestBook(GuestBookUpdateRequest request) {
		GuestBookResponse response = new GuestBookResponse();

		try {
			Optional<Guestbook> optional = guestBookDao.findByBoardId(request.getBoardId());
			if (optional.isPresent()) {
				Guestbook guestbook = optional.get();
				
				guestbook.setContent(request.getContent());//내용 수정
				guestBookDao.save(guestbook);
				
				response.setBoardId(guestbook.getBoardId());
				response.setContent(guestbook.getContent());
				response.setWriteDate(guestbook.getWriteDate());
				response.setNickName(userDao.findNickNameByUserId(guestbook.getUserId()));
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 업데이트 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}
		return ResponseEntity.ok().body(new CommonResponse<GuestBookResponse>(response));
	}

	//방명록 삭제
	@Override
	public ResponseEntity<? extends BasicResponse> deleteGuestBook(int boardId) {
		try {
			guestBookDao.deleteByBoardId(boardId);
		} catch (Exception e) {
			logger.error("방명록 삭제 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 에러"));
		}
		return ResponseEntity.ok().body(new CommonResponse<String>("방명록 삭제 완료"));
	}

}
