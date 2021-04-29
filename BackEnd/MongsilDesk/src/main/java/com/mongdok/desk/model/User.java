package com.mongdok.desk.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
public class User {
	
	@Id
	private String userId;
	
	private String nickname;
	
	private String email;
	
	private String category;
	
	private String kakaoId;
	
	private String googleId;
	
}
