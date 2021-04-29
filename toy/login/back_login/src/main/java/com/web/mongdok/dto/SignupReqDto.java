package com.web.mongdok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupReqDto {
	
	private String userId;
	
	private String email;
	
	private String kakaoId;	
}