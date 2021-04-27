package com.mongdok.desk.service;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.model.request.guestbook.GuestBookUpdateRequest;
import com.mongdok.desk.model.request.guestbook.GuestbookCreateRequest;

public interface GuestBookService {

	ResponseEntity<? extends BasicResponse> getGuestBookById(int boardId);

	ResponseEntity<? extends BasicResponse> createGuestBook(GuestbookCreateRequest request);

	ResponseEntity<? extends BasicResponse> updateGuestBook(GuestBookUpdateRequest request);

	ResponseEntity<? extends BasicResponse> deleteGuestBook(int boardId);

}
