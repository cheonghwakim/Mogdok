package com.mongdok.desk.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	public ResponseEntity<? extends BasicResponse> getCalendar(int year, int month, String nickname) {
		List<StudyResponse> response = new ArrayList<StudyResponse>();

		try {
			String userId = userDao.findUserIdByNickname(nickname);

			//string을 date 타입으로 변환
			String from = String.format("%4d-%02d-02", year, month);
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date to = transFormat.parse(from);

			List<Study> list = studyDao.findAllInThisMonth(to, userId);

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
			logger.error("커밋 달력 불러오기 실패 : {}", e);
			return ResponseEntity.ok().body(new ErrorResponse(ErrorCode.FAIL_GET_CALENDAR));
		}
		return ResponseEntity.ok().body(new CommonResponse<List<StudyResponse>>(response));
	}
}
