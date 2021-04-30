package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
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
import com.web.mongdok.service.DeskService;
import com.web.mongdok.service.KakaoAPI;
import com.web.mongdok.utils.JwtUtil;
import com.web.mongdok.utils.RedisUtil;

import io.swagger.annotations.ApiOperation;

 
@CrossOrigin
@RestController
public class KakaoLoginController {
	
    @Autowired
    private KakaoAPI kakaoAPI;
    
    @Autowired
    private AuthService authService;   
    
    @Autowired
    private DeskService deskService;
    
//    @Autowired
//    private JwtUtil jwtUtil; 
     
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/klogin") // 로그인 토큰 발급 -> redis, 쿠키에 저장
    @ResponseBody
    @ApiOperation(value = "로그인 / kakaoAPI에서 accessToken, refreshToken 발급")
    public ResponseEntity<?> klogin(@RequestParam String authorizeCode) {
    	
    	try {
	    	Map<String, String> kakaoAccessToken = kakaoAPI.getAccessToken(authorizeCode);
	    	String accessToken = kakaoAccessToken.get("accessToken");
	    	String refreshToken = kakaoAccessToken.get("refreshToken");
	        Map<String, String> userInfo = kakaoAPI.getUserInfo(accessToken, refreshToken);
	        
	        String isNew = "X";
	        // 만약 유저가 없다면 회원 가입 (소셜 로그인 할 때마다 DB에 접근하는 건 너무 레이턴시가 큼) -> redis로 판단
	        if(redisUtil.getData(userInfo.get("id")) == null) { 
	        	
	        	// 레디스에 없고 db에 있는 경우는 db에서 find 해줘야 함
	        	if(authService.findByKakaoId(userInfo.get("id")).isEmpty()) {
	        		isNew = "O"; // 뉴비
	        	}
	        }
            
	        // 카카오 refreshToken의 유효기간은 30일
            redisUtil.setDataExpire(refreshToken, userInfo.get("email"), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND); // redis에 refresh 토큰 저장
            
            System.out.println("refreshToken:" + refreshToken);
            System.out.println("kakaoId: " + userInfo.get("id"));
            
            Map<String, String> result = new HashMap<>();
            result.put("isNew", isNew);
            result.put("refreshToken", refreshToken);
            result.put("kakaoId", userInfo.get("id"));
            result.put("email", userInfo.get("email"));
	        return new ResponseEntity<>(result, HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
    
    // 실제 회원 가입 (카카오 로그인 버튼 누르고 -> 회원가입 버튼 누르면 // 회원가입, 내책상 초기화)
    @PostMapping("/signup")
    @ApiOperation("회원 가입할 때 정보 저장")
    public ResponseEntity<?> signUp(@RequestBody SignupReqDto user) {
		
    	String uuid = UUID.randomUUID().toString();
    	User newUser = new User();
    	BeanUtils.copyProperties(user, newUser);
    	newUser.setId(uuid);

		authService.signUpSocialUser(newUser); // 회원 가입
        deskService.setDesk(uuid, user.getPromise()); // 내 책상 초기화

        redisUtil.setData(user.getKakaoId(), "O"); // 새로운 유저인지 아닌지 판단 위함
    	
    	return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    
    @PostMapping("/login")
    @ApiOperation("로그인")
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

    // 일단 필요 없음
//    @GetMapping("/auth")
//    @ApiOperation("토큰 정보 보기 (액세스 토큰의 유효성 검증하거나 정보 확인하는 API)")
//    public ResponseEntity<?> auth(@RequestParam String accessToken) {
//		
//    	System.out.println("토큰 정보보기: " + kakaoAPI.auth(accessToken));
//    	
//		return new ResponseEntity<>("success", HttpStatus.OK);
//    }

//    // 얘도 필요 없을 것 같음
//    @GetMapping("/fresh")
//    @ApiOperation("토큰 갱신하기")
//    public ResponseEntity<?> fresh(@RequestParam String refreshToken) {
//		
//    	System.out.println("토큰 갱신하기: " + kakaoAPI.freshToken(refreshToken));
//    	
//		return new ResponseEntity<>("success", HttpStatus.OK);
//    }

    @GetMapping("/unlink")
    @ApiOperation("카카오 연결 끊기(탈퇴)")
    public ResponseEntity<?> exit(@RequestParam String accessToken) {
		
    	System.out.println("토큰 갱신하기: " + kakaoAPI.unlink(accessToken));
    	// db에서 삭제
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
}