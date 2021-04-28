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

	@GetMapping
	@ApiOperation(value = "나의 책상 모든 것 불러오기(메모, 디데이, 방명록, ..)")
	public ResponseEntity<? extends BasicResponse> getCalendar(int year, int month, String nickname) {
		return calendarService.getCalendar(year, month, nickname);
	}
}
