package com.web.mongdok.dto;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.web.mongdok.entity.Desk;

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
	
	private String userId;
	
	private String email;
	
	private String kakaoId;
	
	private String userName;
	
	private String category;
	
//	private String promise;
	
	private String refreshToken;
	
	private String accessToken;
	
	private Desk desk;
}
