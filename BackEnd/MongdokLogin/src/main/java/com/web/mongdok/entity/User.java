package com.web.mongdok.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@ApiModel
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {
	
	@Id
	@ApiModelProperty(value = "유저 아이디", required = true, example = "d56c6ba4-ad2e-4aeb-b27b-de9ad65d5bb2")
	@Column(name = "id", updatable = false, nullable = false)
	private String userId;
	
	@Column(name = "nickname")
	@ApiModelProperty(value = "유저 닉네임", required = true, example = "안양불바다")
	private String userName;
	
	@ApiModelProperty(value = "유저 이메일", required = false, example = "ssafy@ssafy.com")
	private String email;
	
	@ApiModelProperty(value = "카테고리", required = true, example = "수능")
	private String category;
	
	@ApiModelProperty(value = "카카오 id (카카오에서 부여한 id)", required = true, example = "1710970888")
	private String kakaoId;
	
	private String googleId;
	
//	@OneToOne
//	@ApiModelProperty(value = "유저 아이디", required = true, example = "d56c6ba4-ad2e-4aeb-b27b-de9ad65d5bb2")
//	@JoinColumn(name = "userId")
//	private Desk desk;
}
