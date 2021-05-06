package com.mongdok.desk.model.response.memo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemoResponse {
	private long memoId;
	
	private String content;
	
	private String transform;
	
	private Integer color;
}
