package com.mongdok.desk.model.response.board;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
	private long boardId;
	
	private String content;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date writeDate;
	
	private String userName;
	
	private boolean read;
}
