package com.mongdok.desk.service;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.board.BoardCreateRequest;
import com.mongdok.desk.model.request.board.BoardUpdateRequest;

public interface BoardService {

	ResponseEntity<? extends BasicResponse> getGuestBookById(long boardId);

	ResponseEntity<? extends BasicResponse> createGuestBook(BoardCreateRequest request);

	ResponseEntity<? extends BasicResponse> updateGuestBook(BoardUpdateRequest request);

	ResponseEntity<? extends BasicResponse> deleteGuestBook(long boardId);

}
