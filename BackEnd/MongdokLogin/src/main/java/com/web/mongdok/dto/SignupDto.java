package com.web.mongdok.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입 양식
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class SignupDto {
	
	@ApiModelProperty(value = "유저 카카오 아이디", required = true, example = "1710970888")
	private String kakaoId;
	
	@ApiModelProperty(value = "유저 닉네임", required = true, example = "안양불바다")
	private String userName;
	
	@ApiModelProperty(value = "카테고리", required = true, example = "수능")
	private String category;
	
	@ApiModelProperty(value = "다짐", required = true, example = "장관상 타자!")
	private String promise;
}