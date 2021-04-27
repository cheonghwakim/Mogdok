package com.web.mongdok.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
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
    public HashMap<String, String> klogin(@RequestParam String authorize_code) {
    	String access_token = kakaologin.getAccessToken(authorize_code);
        HashMap<String, String> userinfo = kakaologin.getUserInfo(access_token);
//        System.out.println(userinfo.get("email"));
        
        SignupReqDto user = new SignupReqDto();
        user.setEmail(userinfo.get("email"));
        user.setId(userinfo.get("id"));
        user.setAccess_token(access_token);
        
        final String token = JwtService.create(user);
        redisUtil.setDataExpire(user.getEmail(), token, JwtService.REFRESH_TOKEN_VALIDATION_SECOND);
        
        return userinfo;
    }
    
    @PostMapping("/login")
    public String login(@RequestBody SignupReqDto form) {
    	String access_token;
		String email = null;
		String id;
		
    	try {
    		access_token = form.getAccess_token();
    		email = form.getEmail();
    		id = form.getId();
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
        return email;
    }
}