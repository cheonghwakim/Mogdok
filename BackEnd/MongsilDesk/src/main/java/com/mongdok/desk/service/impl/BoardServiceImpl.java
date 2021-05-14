package com.mongdok.desk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	private BoardDao boardDao;
	@Autowired
	private UserDao userDao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	// 방명록 id로 방명록 정보 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getBoardById(long boardId) {
		BoardResponse response = new BoardResponse();

		try {
			Optional<Board> optional = boardDao.findByBoardId(boardId);
			if (optional.isPresent()) {
				Board board = optional.get();

				response.setBoardId(board.getBoardId());
				response.setContent(board.getContent());
				response.setWriteDate(board.getWriteDate());
				response.setUserName(userDao.findUserNameByUserId(board.getUserId()));// userid를 nickname으로 바꿔보내기
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 불러오기 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_BOARD));
		}

		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	// 방명록 작성
	@Override
	public ResponseEntity<? extends BasicResponse> createBoard(BoardCreateRequest request, String userId) {
		BoardResponse response = new BoardResponse();

		try {
			Board board = new Board();
			board.setContent(request.getContent());
			board.setDeskId(request.getDeskId());
			board.setUserId(userId);// nickname을 userid 찾아옴
			board.setRead(false);// nickname을 userid 찾아옴
			boardDao.save(board);

			response.setBoardId(board.getBoardId());
			response.setContent(board.getContent());
			response.setWriteDate(board.getWriteDate());
			response.setUserName(userDao.findUserNameByUserId(userId));
			response.setRead(board.isRead());

		} catch (Exception e) {
			logger.error("방명록 작성 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	// 방명록 업데이트
	@Override
	public ResponseEntity<? extends BasicResponse> updateBoard(BoardUpdateRequest request, String userId) {
		BoardResponse response = new BoardResponse();

		try {
			Optional<Board> optional = boardDao.findByBoardId(request.getBoardId());
			if (optional.isPresent()) {
				Board board = optional.get();

				board.setContent(request.getContent());// 내용 수정
				boardDao.save(board);

				response.setBoardId(board.getBoardId());
				response.setContent(board.getContent());
				response.setWriteDate(board.getWriteDate());
				response.setUserName(userDao.findUserNameByUserId(userId));
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 업데이트 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_UPDATE_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<BoardResponse>(response));
	}

	// 방명록 삭제
	@Override
	public ResponseEntity<? extends BasicResponse> deleteBoard(long boardId) {
		try {
			boardDao.deleteByBoardId(boardId);
		} catch (Exception e) {
			logger.error("방명록 삭제 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_DELETE_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<String>("방명록 삭제 완료"));
	}

	// 방명록 읽음처리
	@Override
	public ResponseEntity<? extends BasicResponse> readBoard(long boardId) {
		try {
			Optional<Board> optional = boardDao.findByBoardId(boardId);

			if (optional.isPresent()) {
				Board board = optional.get();
				board.setRead(true);// 방명록 읽음
				boardDao.save(board);

			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 boardId"));
			}
		} catch (Exception e) {
			logger.error("방명록 읽음처리 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_READ_BOARD));
		}
		return ResponseEntity.ok().body(new CommonResponse<String>("방명록 읽음 처리 완료"));
	}

	// 모든 방명록 불러오기
	@Override
	public ResponseEntity<? extends BasicResponse> getBoardByDeskId(Pageable pageable, long deskId) {
		List<BoardResponse> boardList = new ArrayList<>();

		try {
			Page<Board> page = boardDao.findAllByDeskId(pageable, deskId);
			if (!page.isEmpty()) {
				for (Board board : page.getContent()) {
					BoardResponse response = BoardResponse.builder()
							.boardId(board.getBoardId())
							.content(board.getContent())
							.writeDate(board.getWriteDate())
							.userName(userDao.findUserNameByUserId(board.getUserId()))// userid를 nickname으로 바꿔보내기
							.read(board.isRead())
							.build();

					boardList.add(response);
				}
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("존재하지 않는 deskId"));
			}
		} catch (Exception e) {
			logger.error("방명록 불러오기 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_ALLBOARD));
		}

		return ResponseEntity.ok().body(new CommonResponse<Page<BoardResponse>>(new PageImpl<BoardResponse>(boardList)));
	}

}
