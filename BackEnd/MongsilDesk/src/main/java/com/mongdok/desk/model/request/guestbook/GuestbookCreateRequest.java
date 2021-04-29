package com.mongdok.desk.model.request.guestbook;

import lombok.Data;

@Data
public class GuestbookCreateRequest {
	private String content;
	private long deskId;
	private String nickName;
}
