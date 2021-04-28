package com.mongdok.desk.model.request.guestbook;

import lombok.Data;

@Data
public class GuestBookUpdateRequest {
	private String content;
	private long boardId;
}
