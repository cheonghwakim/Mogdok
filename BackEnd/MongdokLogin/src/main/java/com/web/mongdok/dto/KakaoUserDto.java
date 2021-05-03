package com.web.mongdok.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoUserDto {
	
	private String accessToken;
	
	private String refreshToken;
	
	private String kakaoId;
}
