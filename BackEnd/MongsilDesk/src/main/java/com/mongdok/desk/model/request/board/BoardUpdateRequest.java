package com.mongdok.desk.model.request.board;

import lombok.Data;

@Data
public class BoardUpdateRequest {
	private String content;
	private long boardId;
}
