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
import com.web.mongdok.service.KakaoAPI;
 
@CrossOrigin
@RestController
public class KaKaoLoginController {
	
    @Autowired
    private KakaoAPI kakaologin;
    
    @GetMapping("/klogin")
    public HashMap<String, String> klogin(@RequestParam String authorize_code) {
    	String access_token = kakaologin.getAccessToken(authorize_code);
        HashMap<String, String> userinfo = kakaologin.getUserInfo(access_token);
        
        System.out.println(userinfo.get("email"));
        return userinfo;
    }
    
    @PostMapping("/login")
    public String login(@RequestBody SignupReqDto form) {
    	String pass;
		String email = null;
		String uname;
		
    	try {
    		pass = form.getPassword();
    		email = form.getEmail();
    		uname = form.getUname();
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
        return email;
    }
    
}