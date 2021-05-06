package com.mongdok.desk.common.response;

import com.mongdok.desk.exception.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {
	private String errorName;
	private String errorMessage;

	public ErrorResponse(ErrorCode code) {
		this.errorName = code.getName();
		this.errorMessage = code.getMessage();
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code);
	}

}
