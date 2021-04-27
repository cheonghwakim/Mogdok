package com.web.mongdok.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.dto.SignupReqDto;
import com.web.mongdok.service.JwtService;
import com.web.mongdok.service.KakaoAPI;
import com.web.mongdok.utils.RedisUtil;
 
@CrossOrigin
@RestController("/oauth")
public class KaKaoLoginController {
	
    @Autowired
    private KakaoAPI kakaologin;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @GetMapping("/klogin") // 로그인 토큰 발급
    public ResponseEntity<?> klogin(@RequestParam String authorizeCode) {
    	
    	try {
	    	String accessToken = kakaologin.getAccessToken(authorizeCode);
	        HashMap<String, String> userInfo = kakaologin.getUserInfo(accessToken);
	//        System.out.println(userinfo.get("email"));
	        
	        SignupReqDto user = new SignupReqDto(userInfo.get("email"), accessToken, userInfo.get("id"));
	        
	        final String token = JwtService.create(user);
	        redisUtil.setDataExpire(user.getEmail(), token, JwtService.REFRESH_TOKEN_VALIDATION_SECOND);
        	
	        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupReqDto form) {
		
    	try {
    		
    		if(form != null)
    			return new ResponseEntity<>(form, HttpStatus.OK);
    		else
    			return new ResponseEntity<>("null", HttpStatus.ACCEPTED);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
}