package com.mongdok.desk.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	OK(200, "OK"),
	BAD_REQUEST(400, "BAD_REQUEST"),
	INTERNAL_SERVER_ERROR(500, "서버 내부 에러."),
	NOT_FOUND(404, "NOT_FOUND"),
    UNEXPECTED_USER(1000, "존재하지 않는 회원입니다.");
	
    private final Integer code;
    private final String message;
    
	private ErrorCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
    
   
}
