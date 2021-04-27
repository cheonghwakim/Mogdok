package com.mongdok.desk.model.request.memo;

import lombok.Data;

@Data
public class MemoCreateRequest {
	private int deskId;
	private String content;
}
