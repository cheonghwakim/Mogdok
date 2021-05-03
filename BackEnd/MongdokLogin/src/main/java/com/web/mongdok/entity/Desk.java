package com.web.mongdok.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@Table
@ApiModel
public class Desk {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@ApiModelProperty(value = "책상 아이디", required = true, example = "4")
	private Long id;

	@ApiModelProperty(value = "유저 아이디", required = true, example = "d56c6ba4-ad2e-4aeb-b27b-de9ad65d5bb2")
	private String userId; // 일대일 맵핑
	
	@ApiModelProperty(value = "다짐", required = true, example = "장관상 타자!")
	private String promise;
	
//	@OneToOne
//	@JoinColumn(name = "id")
//	private User user;

}
