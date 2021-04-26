package com.web.mongdok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupReqDto {
	private String email;
	private String password;
	private String uname;
	
}
