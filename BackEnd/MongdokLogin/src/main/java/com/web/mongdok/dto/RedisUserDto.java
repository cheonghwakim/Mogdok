package com.web.mongdok.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


// redis에 저장할 객체
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisUserDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String email;
	
	private String kakaoId;
	
	private String userName;
	
	private String category;
	
	private String promise;
	
	private String refreshToken;
	
	private String accessToken;
}
