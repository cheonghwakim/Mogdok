package com.mongdok.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.service.CalendarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "커밋 달력", description = "커밋 달력 불러오기")
@CrossOrigin
@RestController
@RequestMapping("/calender")
public class CalendarController {
	@Autowired
	CalendarService calendarService;

	@GetMapping("/month")
	@ApiOperation(value = "커밋 달력 년,월로 불러오기")
	public ResponseEntity<? extends BasicResponse> getCalendarInMonth(int year, int month, String userName) {
		return calendarService.getCalendarInMonth(year, month, userName);
	}

	@GetMapping("/day")
	@ApiOperation(value = "커밋 달력 년,월,일로 불러오기")
	public ResponseEntity<? extends BasicResponse> getCalendarInDay(int year, int month, int day, String userName) {
		return calendarService.getCalendarInDay(year, month, day, userName);
	}
}
