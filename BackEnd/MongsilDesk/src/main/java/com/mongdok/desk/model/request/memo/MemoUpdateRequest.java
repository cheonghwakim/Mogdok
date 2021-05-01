package com.mongdok.desk.model.request.memo;

import lombok.Data;

@Data
public class MemoUpdateRequest {
	private long memoId;
	
	private long desk_Id;
	
	private String content;
	
	private String transform;
	
	private int color;
}
