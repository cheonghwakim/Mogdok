package com.mongdok.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.service.CalendarService;
import com.mongdok.desk.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "커밋 달력", description = "커밋 달력 불러오기")
@CrossOrigin
@RestController
@RequestMapping("/calendar")
public class CalendarController {
	@Autowired
	CalendarService calendarService;
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("/month")
	@ApiOperation(value = "커밋 달력 년,월로 불러오기")
	public ResponseEntity<? extends BasicResponse> getCalendarInMonth(int year, int month,@RequestHeader ("token") String token) {
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);
		return calendarService.getCalendarInMonth(year, month, userId);
	}

	@GetMapping("/day")
	@ApiOperation(value = "커밋 달력 년,월,일로 불러오기")
	public ResponseEntity<? extends BasicResponse> getCalendarInDay(int year, int month, int day,@RequestHeader ("token") String token) {
		if(token==null)
			ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_PROMISE));
		Claims claims = jwtUtil.getClaims(token);
		String userId = claims.get("userId", String.class);
		return calendarService.getCalendarInDay(year, month, day, userId);
	}
}
