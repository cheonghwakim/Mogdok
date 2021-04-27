package com.web.mongdok.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	private String nickname;
	
	private String email;
	
	private String category;
	
	private String kakaoIdd;
	
	private String googleId;
}
