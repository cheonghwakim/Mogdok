package com.mongdok.desk.service;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;

public interface CalendarService {

	ResponseEntity<? extends BasicResponse> getCalendar(int year, int month, String nickname);

}
