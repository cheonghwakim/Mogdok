package com.mongdok.desk.service;

import org.springframework.http.ResponseEntity;

import com.mongdok.desk.common.response.BasicResponse;

public interface CalendarService {

	ResponseEntity<? extends BasicResponse> getCalendarInMonth(int year, int month, String nickname);

	ResponseEntity<? extends BasicResponse> getCalendarInDay(int year, int month, int day, String nickname);

}
