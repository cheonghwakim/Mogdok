package com.mongdok.desk.model.request.board;

import lombok.Data;

@Data
public class BoardCreateRequest {
	private String content;
	private long deskId;
	private String nickname;
}
