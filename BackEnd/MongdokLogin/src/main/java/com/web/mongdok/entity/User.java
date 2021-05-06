package com.web.mongdok.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {
	
	@Id
	private String id;
	
	@Column(name = "nickname")
	private String userName;
	
	private String email;
	
	private String category;
	
	private String kakaoId;
	
	private String googleId;
	
//	@OneToOne(mappedBy = "desk")
//	private Desk desk;
}
