package com.web.mongdok.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private KakaoAPI kakaoAPI;
    
    @Autowired
    private AuthService authService;   
    
//    @Autowired
//    private JwtUtil jwtUtil;
    
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
	    	Map<String, String> kakaoAccessToken = kakaoAPI.getAccessToken(authorizeCode);
	    	String accessToken = kakaoAccessToken.get("accessToken");
	    	String refreshToken = kakaoAccessToken.get("refreshToken");
	        Map<String, String> userInfo = kakaoAPI.getUserInfo(accessToken, refreshToken);
	        
	        String uuid = UUID.randomUUID().toString();
	        
	        // kakaoAccessToken이 너무 길어서 db에 저장 안 됨 

	        SignupReqDto user = new SignupReqDto(uuid, userInfo.get("email"), userInfo.get("id"));
	        
	        // 만약 유저가 없다면 회원 가입
	        if(authService.findByKakaoId(userInfo.get("id")).isEmpty()) {
	        	authService.signUpSocialUser(user); // 회원 가입
	        }
	        
	        User curUser = authService.findByKakaoId(userInfo.get("id")).get();
	        
//            final String token = jwtUtil.generateToken(curUser);
//            final String refreshJwt = jwtUtil.generateRefreshToken(curUser);
            
//            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
//            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            
	        Cookie accessCookie = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, accessToken);
	        Cookie refreshCookie = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, refreshToken);
//            redisUtil.setDataExpire(refreshJwt, curUser.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND); // redis에 refresh 토큰 저장
            redisUtil.setDataExpire(refreshToken, curUser.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND); // redis에 refresh 토큰 저장
            res.addCookie(accessCookie);
            res.addCookie(refreshCookie);
            
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

    // 로그 아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String accessToken) {
		
    	System.out.println(kakaoAPI.Logout(accessToken));
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    // 카카오 토큰 정보보기 [지금 있는 refresh token값이 유효한지 판단 위함] (액세스 토큰의 유효성 검증하거나 정보 확인하는 API)
    @GetMapping("/auth")
    public ResponseEntity<?> auth(@RequestParam String accessToken) {
		
    	System.out.println("토큰 정보보기: " + kakaoAPI.auth(accessToken));
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    // 토큰 갱신하기
    @GetMapping("/fresh")
    public ResponseEntity<?> fresh(@RequestParam String refreshToken) {
		
    	System.out.println("토큰 갱신하기: " + kakaoAPI.freshToken(refreshToken));
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 연결 끊기 (탈퇴)
    @GetMapping("/unlink")
    public ResponseEntity<?> exit(@RequestParam String accessToken) {
		
    	System.out.println("토큰 갱신하기: " + kakaoAPI.unlink(accessToken));
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
}