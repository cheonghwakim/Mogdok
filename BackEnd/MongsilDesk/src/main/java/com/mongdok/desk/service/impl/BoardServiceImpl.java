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
import com.mongdok.desk.dao.BoardDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.Board;
import com.mongdok.desk.model.request.board.BoardCreateRequest;
import com.mongdok.desk.model.request.board.BoardUpdateRequest;
import com.mongdok.desk.model.response.board.BoardResponse;
import com.mongdok.desk.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDao guestBookDao;
	@Autowired
	private UserDao userDao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	//방명록 id로 방명록 정보 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getGuestBookById(long boardId) {
		BoardResponse response = new BoardResponse();

		try {
			Optional<Board> optional = guestBookDao.findByBoardId(boardId);
			if (optional.isPresent()) {
				Board board = optional.get();

				response.setBoardId(board.getBoardId());
				response.setContent(board.getContent());
				response.setWriteDate(board.getWriteDate());
				response.setNickname(userDao.findNickNameByUserId(board.getUserId()));//userid를 nickname으로 바꿔보내기
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 불러오기 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_GET_BOARD));
		}

		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	// 방명록 작성
	@Override
	public ResponseEntity<? extends BasicResponse> createGuestBook(BoardCreateRequest request) {
		BoardResponse response = new BoardResponse();

		try {
			Board board = new Board();
			board.setContent(request.getContent());
			board.setDeskId(request.getDeskId());
			board.setUserId(userDao.findUserIdByNickname(request.getNickname()));//nickname을 userid 찾아옴
			guestBookDao.save(board);
			
			response.setBoardId(board.getBoardId());
			response.setContent(board.getContent());
			response.setWriteDate(board.getWriteDate());
			response.setNickname(request.getNickname());

		} catch (Exception e) {
			logger.error("방명록 작성 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_GET_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	//방명록 업데이트
	@Override
	public ResponseEntity<? extends BasicResponse> updateGuestBook(BoardUpdateRequest request) {
		BoardResponse response = new BoardResponse();

		try {
			Optional<Board> optional = guestBookDao.findByBoardId(request.getBoardId());
			if (optional.isPresent()) {
				Board board = optional.get();
				
				board.setContent(request.getContent());//내용 수정
				guestBookDao.save(board);
				
				response.setBoardId(board.getBoardId());
				response.setContent(board.getContent());
				response.setWriteDate(board.getWriteDate());
				response.setNickname(userDao.findNickNameByUserId(board.getUserId()));
			}
			else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 업데이트 실패 : {}", e);
			return ResponseEntity.ok()
					.body(new ErrorResponse(ErrorCode.FAIL_UPDATE_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	//방명록 삭제
	@Override
	public ResponseEntity<? extends BasicResponse> deleteGuestBook(long boardId) {
		try {
			guestBookDao.deleteByBoardId(boardId);
		} catch (Exception e) {
			logger.error("방명록 삭제 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(ErrorCode.FAIL_DELETE_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<String>("방명록 삭제 완료"));
	}

}
