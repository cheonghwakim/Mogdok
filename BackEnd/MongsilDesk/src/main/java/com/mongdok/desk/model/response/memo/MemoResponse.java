package com.mongdok.desk.model.response.memo;

import lombok.Data;

@Data
public class MemoResponse {
	private long memoId;
	
	private String content;
	
	private String transform;
	
	private Integer color;
}
