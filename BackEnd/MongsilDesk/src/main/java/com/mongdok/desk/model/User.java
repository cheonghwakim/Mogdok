package com.mongdok.desk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {
	
	@Id
	@Column(name = "id")
	private String userId;
	
	@Column(name="nickname")
	private String userName;
	
	private String email;
	
	private String category;
	
	private String kakaoId;
	
	private String googleId;
	
}
