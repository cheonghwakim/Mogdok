package com.web.mongdok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입 양식
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupDto {
	
	private String userName;
	
	private String category;
	
	private String promise;
}