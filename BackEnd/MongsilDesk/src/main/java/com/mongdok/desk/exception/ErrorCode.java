package com.mongdok.desk.exception;

public enum ErrorCode {

	FAIL_DELETE_DDAY(1000, "FAIL_DELETE_DDAY", "디데이 삭제 실패"), 
	FAIL_CREATE_DDAY(1100, "FAIL_CREATED_DDAY", "디데이 생성 실패"),
	FAIL_GET_PROMISE(2000, "FAIL_GET_DESK", "다짐 불러오기 실패"), 
	FAIL_UPDATE_PROMISE(2100, "FAIL_UPDATE_DESK", "다짐 수정 실패"),
	FAIL_GET_DESK(2200, "FAIL_GET_DESK", "내 책상 정보 불러오기 실패"),
	FAIL_GET_GUESTBOOK(3000, "FAIL_GET_GUESTBOOK", "방명록 불러오기 실패"),
	FAIL_CREATE_GUESTBOOK(3100, "FAIL_GET_GUESTBOOK", "방명록 불러오기 실패"),
	FAIL_UPDATE_GUESTBOOK(3200, "FAIL_UPDATE_GUESTBOOK", "방명록 업데이트 실패"),
	FAIL_DELETE_GUESTBOOK(3300, "FAIL_DELETE_GUESTBOOK", "방명록 삭제 실패"),
	FAIL_DELETE_MEMO(4000, "FAIL_DELETE_MEMO", "메모 삭제 실패"), 
	FAIL_CREATE_MEMO(4100, "FAIL_CREATE_MEMO", "메모 생성 실패"),
	FAIL_UPDATE_MEMO(4200, "FAIL_UPDATE_MEMO", "메모 수정 실패"), 
	FAIL_GET_CALENDAR_MONTH(5000, "FAIL_GET_CALENDAR_MONTH", "해당 년,월 달력 불러오기 실패"),
	FAIL_GET_CALENDAR_DAY(5100, "FAIL_GET_CALENDAR_DAY", "해당 날짜 달력 불러오기 실패");

	private final int code;
	private final String name;
	private final String message;

	ErrorCode(final int code, final String name, final String message) {
		this.code = code;
		this.name = name;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}
}