package com.web.mongdok.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {
	
	@Id
	private String id;
	
	private String nickname;
	
	private String email;
	
	private String category;
	
	private String kakaoId;
	
	private String googleId;
}
