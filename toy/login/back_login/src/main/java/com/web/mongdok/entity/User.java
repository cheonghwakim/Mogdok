package com.web.mongdok.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	private String nickname;
	
	private String email;
	
	private String category;
	
	private String kakao_id;
	
	private String google_id;
}
