package com.mongdok.desk.model.response.dday;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DdayResponse {
	private long ddayId;

	private String title;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date finishDate;

	private String transform;

	private int color;
}
