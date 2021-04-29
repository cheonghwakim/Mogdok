package com.mongdok.desk.model.request.memo;

import lombok.Data;

@Data
public class MemoCreateRequest {
	private long deskId;
	private String content;
}
