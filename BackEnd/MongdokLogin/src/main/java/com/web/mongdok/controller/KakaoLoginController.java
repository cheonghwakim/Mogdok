package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.mongdok.dto.RedisUserDto;
import com.web.mongdok.dto.SignupDto;
import com.web.mongdok.entity.User;
import com.web.mongdok.service.AuthService;
import com.web.mongdok.service.DeskService;
import com.web.mongdok.service.KakaoAPI;
import com.web.mongdok.utils.JwtUtil;
import com.web.mongdok.utils.RedisUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

 
@CrossOrigin
@RestController
public class KakaoLoginController {
	
    @Autowired
    private KakaoAPI kakaoAPI;
    
    @Autowired
    private AuthService authService;   
    
    @Autowired
    private DeskService deskService;
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private JwtUtil jwtUtil; 
     
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/klogin") // 로그인 토큰 발급 -> redis, 쿠키에 저장
    @ResponseBody
    @ApiOperation(value = "로그인 / kakaoAPI에서 accessToken, refreshToken 발급")
    public ResponseEntity<?> klogin(@RequestParam @ApiParam(value = "프론트에서 전달받은 authCode") String authorizeCode) {
    	
    	try {
	    	Map<String, String> kakaoAccessToken = kakaoAPI.getAccessToken(authorizeCode);
	    	String accessToken = kakaoAccessToken.get("accessToken");
	    	String refreshToken = kakaoAccessToken.get("refreshToken");
	        Map<String, String> userInfo = kakaoAPI.getUserInfo(accessToken, refreshToken);
	        
	        String isNew = "X";
	        // 만약 유저가 없다면 회원 가입 (소셜 로그인 할 때마다 DB에 접근하는 건 너무 레이턴시가 큼) -> redis로 판단 // 레디스에 없고 db에 있는 경우는 db에서 find 해줘야 함
	        User user = authService.findByKakaoId(userInfo.get("id"));
	        if(redisUtil.getData(userInfo.get("id")) == null) { 
	        	
	        	if(user == null)// 뉴비
	        		isNew = "O"; 
	        	
	        	else { // redis에 없고 db에 있음 -> redis에 정보 저장

	        		RedisUserDto redisUser = new RedisUserDto();
	            	BeanUtils.copyProperties(user, redisUser);
	            	
	            	String jwtRefreshToken = jwtUtil.generateRefreshToken(refreshToken);
	            	redisUtil.setData(userInfo.get("id"), "O");
	            	redisUtil.setObjectExpire(jwtRefreshToken, redisUser, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
	        	}
	        
	        } // else redis에 있고 db에 있음

	        
            System.out.println("refreshToken:" + refreshToken);
            System.out.println("kakaoId: " + userInfo.get("id"));
            
            Map<String, String> result = new HashMap<>();
            result.put("isNew", isNew);
            result.put("refreshToken", refreshToken);
            result.put("accessToken", refreshToken);
            result.put("kakaoId", userInfo.get("id"));
            result.put("email", userInfo.get("email"));
	        return new ResponseEntity<>(result, HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @PostMapping("/nickname")
    @ApiOperation("닉네임 중복 처리")
    public ResponseEntity<?> nickname(@RequestBody @ApiParam(value = "유저의 닉네임") String nickname) {

    	if(authService.findByNickname(nickname))
    		return new ResponseEntity<>("overlap", HttpStatus.OK);
    	return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    // 실제 회원 가입 (카카오 로그인 버튼 누르고 -> 회원가입 버튼 누르면 // 회원가입, 내책상 초기화) (isNew가 O일 때만)
    @PostMapping("/signup")
    @ApiOperation("회원 가입할 때 정보 저장 or 마이페이지에서 정보 수정")
    public ResponseEntity<?> signUp(@RequestBody @ApiParam(value = "회원 가입 form에서 얻은 객체") SignupDto user) {
		
    	String uuid = UUID.randomUUID().toString();
    	User newUser = new User();
    	BeanUtils.copyProperties(user, newUser);
    	newUser.setId(uuid);

		authService.signUpSocialUser(newUser); // 회원 가입
        deskService.setDesk(uuid, user.getPromise()); // 내 책상 초기화

        // 닉네임 중복 처리
        
        
        redisUtil.setData(user.getKakaoId(), "O"); // 새로운 유저인지 아닌지 판단 위함
    	
    	// redis에 정보 저장
    	RedisUserDto redisUser = new RedisUserDto();
    	BeanUtils.copyProperties(user, redisUser);
    	redisUser.setId(uuid);
    	
    	System.out.println(redisUser);
    	System.out.println("refreshToken: " + user.getRefreshToken());
    	String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getRefreshToken());
    	// 카카오 refreshToken의 유효기간은 30일
    	redisUtil.setObjectExpire(jwtRefreshToken, redisUser, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
    	
    	return new ResponseEntity<>(jwtRefreshToken, HttpStatus.OK);
    }
    
    @GetMapping("/auth")
    @ApiOperation("레디스에서 인증하기")
    public ResponseEntity<?> auth(@RequestParam @ApiParam(value = "유저가 가진 refreshToken") String jwtRefreshToken) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	String userInfo = redisUtil.getData(jwtRefreshToken);
    	if(userInfo == null)
    		return new ResponseEntity<>("fail", HttpStatus.OK);
    	
    	System.out.println(userInfo);
    	RedisUserDto user = new RedisUserDto();
    	try {
			user = objectMapper.readValue(userInfo, RedisUserDto.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation("로그인")
    public ResponseEntity<?> login(@RequestBody @ApiParam(value = "?? 일단 필요 없을 듯..?") SignupDto form) {
		
    	try {
    		if(form == null)
    			return new ResponseEntity<>("null", HttpStatus.ACCEPTED);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    	
    	return new ResponseEntity<>(form, HttpStatus.OK);
    }

    @GetMapping("/unlink")
    @ApiOperation("카카오 연결 끊기(탈퇴)")
    public ResponseEntity<?> exit(@RequestParam @ApiParam(value = "유저의 accessToken") String accessToken) {
		
    	System.out.println("토큰 갱신하기: " + kakaoAPI.unlink(accessToken));
    	// db에서 삭제
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
}