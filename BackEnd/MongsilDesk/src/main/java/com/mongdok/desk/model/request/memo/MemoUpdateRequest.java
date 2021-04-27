package com.mongdok.desk.model.request.memo;

import lombok.Data;

@Data
public class MemoUpdateRequest {
	private int memoId;
	private String content;
}
