package com.web.mongdok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupDto {
	
	private String refreshToken;
	
	private String email;
	
	private String kakaoId;
	
	private String nickname;
	
	private String category;
	
	private String promise;
}