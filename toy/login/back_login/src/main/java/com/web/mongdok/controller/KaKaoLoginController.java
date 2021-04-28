package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.dto.SignupReqDto;
import com.web.mongdok.entity.User;
import com.web.mongdok.service.AuthService;
import com.web.mongdok.service.KakaoAPI;
import com.web.mongdok.utils.CookieUtil;
import com.web.mongdok.utils.JwtUtil;
import com.web.mongdok.utils.RedisUtil;

import javassist.NotFoundException;

 
@CrossOrigin
@RestController
public class KaKaoLoginController {
	
    @Autowired
    private KakaoAPI kakaologin;
    
    @Autowired
    private AuthService authService;   
    
    @Autowired
    private JwtUtil jwtUtil;
    
//    @Autowired
//    private JwtService jwtService;
    
     @Autowired
    private CookieUtil cookieUtil;   
     
    @Autowired
    private RedisUtil redisUtil;
    
    // 임시
    @GetMapping("/getUser")
	public ResponseEntity<?> user() {
	    List<User> user = authService.findAll();
    	
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    // 임시
    @GetMapping("/getUsers")
	public User userasd() throws NotFoundException {
	    return authService.findByUserId("asdfsadf");
    }

    @GetMapping("/klogin") // 로그인 토큰 발급 -> redis, 쿠키에 저장
    @ResponseBody
    public ResponseEntity<?> klogin(@RequestParam String authorizeCode, HttpServletResponse res) {
    	
    	try {
	    	Map<String, String> kakaoAccessToken = kakaologin.getAccessToken(authorizeCode);
	        Map<String, String> userInfo = kakaologin.getUserInfo(kakaoAccessToken.get("accessToken"), kakaoAccessToken.get("refreshToken"));
	        
	        // kakaoAccessToken이 너무 길어서 db에 저장 안 됨
	        SignupReqDto user = new SignupReqDto(userInfo.get("email"), "asdfasdfsa", "ekekekek", userInfo.get("id"));
	        
	        // 만약 유저가 있다면 로그인, 없다면 회원 가입
	        if(authService.findByUserId(userInfo.get("id")) == null) {
	        	authService.signUpSocialUser(user); // 회원 가입
	        }
	        
	        User curUser = authService.findByUserId(userInfo.get("id"));
	        
            final String token = jwtUtil.generateToken(curUser);
            final String refreshJwt = jwtUtil.generateRefreshToken(curUser);
            
            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            redisUtil.setDataExpire(refreshJwt, curUser.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND); // redis에 refresh 토큰 저장
            res.addCookie(accessToken);
            res.addCookie(refreshToken);
            
	        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupReqDto form) {
		
    	try {
    		if(form == null)
    			return new ResponseEntity<>("null", HttpStatus.ACCEPTED);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    	
    	return new ResponseEntity<>(form, HttpStatus.OK);
    }
    
    
    
    
    
    
    
    
    
    
    
//    @GetMapping("/klogin") // 로그인 토큰 발급
//    public ResponseEntity<?> klogin(@RequestParam String authorizeCode) {
//    	
//    	try {
//	    	String accessToken = kakaologin.getAccessToken(authorizeCode);
//	        HashMap<String, String> userInfo = kakaologin.getUserInfo(accessToken);
//	//        System.out.println(userinfo.get("email"));
//	        
//	        SignupReqDto user = new SignupReqDto(userInfo.get("email"), accessToken, userInfo.get("id"));
//	        
//	        final String token = JwtService.create(user);
//	        redisUtil.setDataExpire(user.getEmail(), token, JwtService.REFRESH_TOKEN_VALIDATION_SECOND);
//        	
//	        return new ResponseEntity<>(userInfo, HttpStatus.OK);
//    	
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
//    	}
//    }
}