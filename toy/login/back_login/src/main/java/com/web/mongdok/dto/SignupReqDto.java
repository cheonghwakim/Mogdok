package com.web.mongdok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignupReqDto {
	

	private String email;
	private String access_token;
	private String id;
	
}