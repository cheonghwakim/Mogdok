package com.mongdok.desk.model.request.dday;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DdayRequest {
	private long ddayId;
	
	private long deskId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date finishDate;
	
	private String title;
	
	private String transform;
	
	private Integer color;
}
