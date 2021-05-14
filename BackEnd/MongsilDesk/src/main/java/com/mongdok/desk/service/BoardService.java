package com.mongdok.desk.service;


import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.board.BoardCreateRequest;
import com.mongdok.desk.model.request.board.BoardUpdateRequest;

public interface BoardService {

	ResponseEntity<? extends BasicResponse> getBoardById(long boardId);

	ResponseEntity<? extends BasicResponse> createBoard(BoardCreateRequest request, String userId);

	ResponseEntity<? extends BasicResponse> updateBoard(BoardUpdateRequest request, String userId);

	ResponseEntity<? extends BasicResponse> deleteBoard(long boardId);

	ResponseEntity<? extends BasicResponse> readBoard(long boardId);

	ResponseEntity<? extends BasicResponse> getBoardByDeskId(Pageable pageable, long deskId);

}
