package com.web.mongdok.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.service.AuthService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@CrossOrigin
@RequestMapping("redis")
public class RedisController {

	@Value("${CF_INSTANCE_IP:127.0.0.1}") 
	private String ip;
	
	@Autowired
	KakaoLoginController kloginController;
	
    @Autowired
    private AuthService authService;  
    
	// user auth (key: 쿠키에 저장된 refresh_token)
//	@PostMapping("/userVerify")
//	@ApiOperation(value = "유저가 유효한지 검사")
//    public ResponseEntity<?> userVerify(@RequestParam @ApiParam(value = "유저가 가진 refreshToken") String jwtRefreshToken, HttpServletResponse res) {
//		
//		try {
//        	
//            if(authService.VerificationUser(jwtRefreshToken).equals("success"))
//            	return new ResponseEntity<>("exist", HttpStatus.OK);
//        
//        } catch (Exception e) {
//        	e.printStackTrace();
//            return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
//        }
//        
//        return new ResponseEntity<>("unknown", HttpStatus.OK); // 이러면 다시 로그인 해야 함
//    }
}