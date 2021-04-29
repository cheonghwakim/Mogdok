package com.mongdok.desk.model.request.memo;

import lombok.Data;

@Data
public class MemoUpdateRequest {
	private long memoId;
	private String content;
}
