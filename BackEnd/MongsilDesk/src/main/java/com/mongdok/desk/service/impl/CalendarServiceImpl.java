package com.mongdok.desk.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongdok.desk.common.response.BasicResponse;
import com.mongdok.desk.common.response.CommonResponse;
import com.mongdok.desk.common.response.ErrorResponse;
import com.mongdok.desk.dao.StudyDao;
import com.mongdok.desk.dao.UserDao;
import com.mongdok.desk.exception.ErrorCode;
import com.mongdok.desk.model.Study;
import com.mongdok.desk.model.response.study.StudyResponse;
import com.mongdok.desk.service.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {

	@Autowired
	StudyDao studyDao;
	@Autowired
	private UserDao userDao;

	public static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);

	@Override
	public ResponseEntity<? extends BasicResponse> getCalendarInMonth(int year, int month, String userName) {
		List<StudyResponse> response = new ArrayList<StudyResponse>();

		try {
			String userId = userDao.findUserIdByUserName(userName);

			LocalDate date=LocalDate.of(year, month, 1);
			List<Study> list = studyDao.findAllInThisMonth(date, userId);

			if (!list.isEmpty()) {
				for (Study study : list) {
					StudyResponse input = new StudyResponse();
					BeanUtils.copyProperties(study, input);
					response.add(input);
				}
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("공부 이력이 존재하지 않습니다."));
			}
		} catch (Exception e) {
			logger.error("년,월 커밋 달력 불러오기 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_CALENDAR_MONTH));
		}
		return ResponseEntity.ok().body(new CommonResponse<List<StudyResponse>>(response));
	}

	@Override
	public ResponseEntity<? extends BasicResponse> getCalendarInDay(int year, int month, int day, String userName) {
		List<StudyResponse> response = new ArrayList<StudyResponse>();

		try {
			String userId = userDao.findUserIdByUserName(userName);

			LocalDate date=LocalDate.of(year, month, day);
			List<Study> list = studyDao.findAllInToday(date, userId);

			if (!list.isEmpty()) {
				for (Study study : list) {
					StudyResponse input = new StudyResponse();
					BeanUtils.copyProperties(study, input);
					response.add(input);
				}
			} else {
				return ResponseEntity.ok().body(new CommonResponse<String>("공부 이력이 존재하지 않습니다."));
			}
		} catch (Exception e) {
			logger.error("년,월,일 커밋 달력 불러오기 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_CALENDAR_DAY));
		}
		return ResponseEntity.ok().body(new CommonResponse<List<StudyResponse>>(response));
	}
}
