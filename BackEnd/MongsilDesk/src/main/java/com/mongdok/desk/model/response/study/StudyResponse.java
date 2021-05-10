package com.mongdok.desk.model.response.study;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyResponse {
	private long studyId;

	private Date studyTime;
	
	private Date startTime;

	private String status;
}
