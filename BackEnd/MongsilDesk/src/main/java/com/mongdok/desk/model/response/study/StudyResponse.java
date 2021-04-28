package com.mongdok.desk.model.response.study;

import java.util.Date;


import lombok.Data;

@Data
public class StudyResponse {
	private long studyId;

	private Date studyTime;
	
	private Date startTime;

	private String status;
}
