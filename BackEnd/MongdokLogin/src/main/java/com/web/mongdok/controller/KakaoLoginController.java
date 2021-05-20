package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import com.web.mongdok.dto.KakaoUserDto;
import com.web.mongdok.dto.RedisUserDto;
import com.web.mongdok.dto.SignupDto;
import com.web.mongdok.dto.UserProfileDto;
import com.web.mongdok.entity.Desk;
import com.web.mongdok.entity.User;
import com.web.mongdok.service.AuthService;
import com.web.mongdok.service.DeskService;
import com.web.mongdok.service.KakaoAPI;
import com.web.mongdok.utils.JwtUtil;
import com.web.mongdok.utils.RedisUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiOperation(value = "로그인", notes="성공 시 kakaoId, 실패 시 false (boolean)")
    public ResponseEntity<?> klogin(@RequestParam @ApiParam(value = "프론트에서 전달받은 authCode") String authorizeCode) {
    	
    	try {
	    	Map<String, String> kakaoAccessToken = kakaoAPI.getAccessToken(authorizeCode);
	    	String accessToken = kakaoAccessToken.get("accessToken");
	    	String refreshToken = kakaoAccessToken.get("refreshToken");
	        Map<String, String> userInfo = kakaoAPI.getUserInfo(accessToken, refreshToken);

//	        String jwtRefreshToken;
	        
//	        KakaoUserDto kakaoUser = new KakaoUserDto();
//	        kakaoUser.setAccessToken(accessToken);
//	        kakaoUser.setRefreshToken(refreshToken);
//	        kakaoUser.setKakaoId(userInfo.get("id"));
	        // 여기서 담겨줘야 되는 것들 생각해보기 (지금: accessToken, refreshToken, kakaoId)
//	        jwtRefreshToken = jwtUtil.doGenerateToken(kakaoUser, JwtUtil.TOKEN_VALIDATION_SECOND);

	        System.out.println("klogin kakaoId: " + userInfo.get("id"));
//	        System.out.println("klogin jwt: " + jwtRefreshToken);
	        return new ResponseEntity<>(userInfo.get("id"), HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @GetMapping("/login")
    @ApiOperation(value = "로그인 요청 (객체가 있다면 user return, 없다면 null, 비어있으면 false)")
    public ResponseEntity<?> login(@RequestParam @ApiParam(value = "유저 kakaoId") String kakaoId) {
    	
    	if(kakaoId == "")
    		return new ResponseEntity<>(false, HttpStatus.OK);
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	String newJwtToken = null;
    	RedisUserDto redisUser = null;
    	Map<String, Object> map = new HashMap<>();
    	
    	Optional<User> user = authService.findByKakaoId(kakaoId);
		if(user.isPresent()) { // user는 null이 아니라면 -> redis에 정보 저장 (jwt)
			
			Desk desk = deskService.findByUserId(user.get().getUserId()); 
	    	redisUser = new RedisUserDto();
	    	redisUser.setDeskId(desk.getDeskId());
	    	redisUser.setPromise(desk.getPromise());
	    	BeanUtils.copyProperties(user.get(), redisUser);  
	    	
	    	KakaoUserDto kakaoUser = new KakaoUserDto();
	    	kakaoUser.setKakaoId(kakaoId);
	    	kakaoUser.setUserId(user.get().getUserId());
	    	kakaoUser.setUserName(user.get().getUserName());
	    	newJwtToken = jwtUtil.doGenerateToken(kakaoUser, JwtUtil.TOKEN_VALIDATION_SECOND);
	    	
	    	redisUser.setAuthToken(newJwtToken);
	    	System.out.println(redisUser);
	    	redisUtil.setObjectExpire(newJwtToken, redisUser, JwtUtil.TOKEN_VALIDATION_SECOND);
	    	
	    	map.put("user", redisUser);
	    	map.put("login", "OK");
	    	return new ResponseEntity<>(map, HttpStatus.OK);
		}	
    		
    	System.out.println("user: " + redisUser);
    	map.put("user", redisUser);
    	map.put("login", "Join");		
    	return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    @GetMapping("/auth")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "auth-token", value = "jwt 토큰", required = true,
                dataType = "string", paramType = "header", defaultValue = "")
    })
    @ApiOperation(value = "jwt로 유저 인증(로그인) (객체가 있다면 user return, 없다면 null)", notes = "auth-token 예시:  ex. eyJhbGciOiJIUzUxMiJ9.eyJrYWthb0lkIjoiMTcxMDk3MDg4OCIsInVzZXJOYW1lIjoiY2hlb25naHdhMiIsInVzZXJJZCI6IjIyMTYwZGEwLWM5ZDgtNDdkMS1iOTgwLTI5N2RmMWY4ODMwNSIsInN1YiI6InVzZXIiLCJpYXQiOjE2MjA2MzY1MTUsImV4cCI6NDIxMjYzNjUxNX0.GMLjTPmUohO0B-az74gm49Ubybh-CJ_yS2of0obPaJEtpTZwv3Y1WM3A8EbP90_tOATnAPwz1Hb0TTJUM2qGIg")
    public ResponseEntity<?> login(HttpServletRequest request) {
    	String jwtToken = request.getHeader("auth-token");

    	String kakaoId = (String) jwtUtil.extractKakaoId(jwtToken);
    	System.out.println("kakaoId: " + kakaoId);
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	RedisUserDto redisUser = null;
    	if(redisUtil.getData(jwtToken) == null) { // redis에서 null
    		Optional<User> user = authService.findByKakaoId(kakaoId);
    		if(user.isPresent()) { // user는 null이 아니라면 -> redis에 정보 저장 (jwt)
    			
    			System.out.println("asfsd");
    			Desk desk = deskService.findByUserId(user.get().getUserId()); 
    	    	redisUser = new RedisUserDto();
    	    	redisUser.setDeskId(desk.getDeskId());
    	    	redisUser.setPromise(desk.getPromise());
    	    	redisUser.setAuthToken(jwtToken);
    	    	BeanUtils.copyProperties(user.get(), redisUser);

    	    	System.out.println(redisUser);
    	    	redisUtil.setObjectExpire(jwtToken, redisUser, JwtUtil.TOKEN_VALIDATION_SECOND);
    		}
    	} else { // redis에 정보 있다면
    		String userInfo = redisUtil.getData(jwtToken);

    		System.out.println(userInfo);
    		try { // redis에서 user 꺼내서 return
				redisUser = objectMapper.readValue(userInfo, RedisUserDto.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

    	}
    		
    	System.out.println("user: " + redisUser);

    	return new ResponseEntity<>(redisUser, HttpStatus.OK);
    }
    
    @GetMapping("/nickname")
    @ApiOperation(value = "닉네임 중복 처리 // 실패 false 성공 true (boolean)")
    public ResponseEntity<?> nickname(@RequestParam @ApiParam(value = "유저의 닉네임") String nickname) {

    	if(authService.findByUserName(nickname) == null)
    		return new ResponseEntity<>(true, HttpStatus.OK);
    	return new ResponseEntity<>(false, HttpStatus.OK);
    }
    
    @PostMapping("/signup")
    @ApiOperation(value = "회원 가입할 때 정보 저장// user 반환")
    public ResponseEntity<?> signUp(@RequestBody @ApiParam(value = "회원 가입 form에서 얻은 객체") SignupDto user) {

    	String kakaoId = user.getKakaoId();
    	User newUser = new User();
    	System.out.println(kakaoId);
    	
    	Optional<User> findUser = authService.findByKakaoId(kakaoId);
    	Desk desk = null;   
    	String newJwtToken = null;
    	RedisUserDto redisUser = new RedisUserDto();
    	if(!findUser.isPresent()) {
	    	String uuid = UUID.randomUUID().toString();

	    	BeanUtils.copyProperties(user, newUser);
	    	newUser.setUserId(uuid);
	    	newUser.setKakaoId(kakaoId);	  
	    	
			authService.signUpSocialUser(newUser); // 회원 가입
			desk = deskService.setDesk(newUser.getUserId(), user.getPromise()); // 내 책상 초기화
			
			System.out.println("newUser: " + newUser);
			System.out.println(desk);
	        // redis에 정보 저장
	    	BeanUtils.copyProperties(newUser, redisUser);
	    	redisUser.setDeskId(desk.getDeskId());
	    	redisUser.setPromise(desk.getPromise());
//	    	redisUser.setAccessToken((String) jwtUtil.extractAllClaims(jwtToken).get("accessToken"));
	
	    	KakaoUserDto kakaoUser = new KakaoUserDto();
	    	kakaoUser.setKakaoId(kakaoId);
	    	kakaoUser.setUserId(uuid);
	    	kakaoUser.setUserName(user.getUserName());
	    	newJwtToken = jwtUtil.doGenerateToken(kakaoUser, JwtUtil.TOKEN_VALIDATION_SECOND);
	    	
	    	redisUser.setAuthToken(newJwtToken);
	    	
	    	System.out.println(redisUser);
	    	redisUtil.setObjectExpire(newJwtToken, redisUser, JwtUtil.TOKEN_VALIDATION_SECOND);
	    	
	    	return new ResponseEntity<>(redisUser, HttpStatus.OK);
    	}    	
    	return new ResponseEntity<>(findUser.get(), HttpStatus.OK);
    }
    
    @PostMapping("/mypage")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "auth-token", value = "jwt 토큰", required = true,
                dataType = "string", paramType = "header", defaultValue = "")
    })
    @ApiOperation("마이페이지에서 정보 수정 // 성공: true, 실패: false (boolean)")
    public ResponseEntity<?> mypage(HttpServletRequest request, @RequestBody @ApiParam(value = "user") SignupDto user) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	System.out.println("jwtToken: " + jwtToken);

    	ObjectMapper objectMapper = new ObjectMapper();
    	String userInfo = redisUtil.getData(jwtToken);
    	
    	RedisUserDto redisUser = null;
		try {
			redisUser = objectMapper.readValue(userInfo, RedisUserDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 

//		System.out.println(redisUser);
    	User changeUser = new User();
    	BeanUtils.copyProperties(redisUser, changeUser);
    	changeUser.setUserName(user.getUserName());
    	changeUser.setCategory(user.getCategory());
    	
    	System.out.println("changeUser: " + changeUser);
    	redisUtil.deleteData(jwtToken);
    	redisUtil.setObjectExpire(jwtToken, redisUser, JwtUtil.TOKEN_VALIDATION_SECOND);
		if(authService.save(changeUser) == null) // 저장
			return new ResponseEntity<>(false, HttpStatus.OK);
	
    	return new ResponseEntity<>(true, HttpStatus.OK);
    }
    
    @GetMapping("/logout")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "auth-token", value = "jwt 토큰", required = true,
                dataType = "string", paramType = "header", defaultValue = "")
    })
    @ApiOperation("로그아웃 -> server: redis에서 유저 정보 삭제, client: jwtToken 정보 삭제")
    public ResponseEntity<?> logout(HttpServletRequest request) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	redisUtil.deleteData(jwtToken);
		return new ResponseEntity<>(true, HttpStatus.OK);
    }
    
    // 어드민 키로 삭제
    @GetMapping("/unlink")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "auth-token", value = "jwt 토큰", required = true,
                dataType = "string", paramType = "header", defaultValue = "")
    })
    @ApiOperation("카카오 연결 끊기(탈퇴) // 성공 시 success")
    public boolean unlinkByAdmin(HttpServletRequest request) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	String kakaoId = (String) jwtUtil.extractAllClaims(jwtToken).get("kakaoId");
    	
    	if(kakaoAPI.unlinkByAdmin(kakaoId))
    		return true;
    	return false;
    }
    
    @GetMapping("/profile")
    @ApiOperation("다른 사람 프로필 가져오기 // 성공 deskId, promise, category 실패 false(boolean)")
    public ResponseEntity<?> profile(@RequestParam @ApiParam(value = "유저의 닉네임") String userName) {
    	UserProfileDto user = deskService.findByUserName(userName);
    	if(user == null) // 찾는 유저가 없으면 false
    		return new ResponseEntity<>(false, HttpStatus.OK);
    	
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }
     
//    @GetMapping("/auth")
//    @ApiOperation("레디스에서 인증하기")
//    public ResponseEntity<?> auth(@RequestParam @ApiParam(value = "유저가 가진 refreshToken") String jwtRefreshToken) {
//    	
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	
//    	String userInfo = redisUtil.getData(jwtRefreshToken);
//    	if(userInfo == null)
//    		return new ResponseEntity<>("fail", HttpStatus.OK);
//    	
//    	System.out.println(userInfo);
//    	RedisUserDto user = new RedisUserDto();
//    	try {
//			user = objectMapper.readValue(userInfo, RedisUserDto.class);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    	return new ResponseEntity<>(user, HttpStatus.OK);
//    }

//    @GetMapping("/unlink")
//    @ApiOperation("카카오 연결 끊기(탈퇴) // 성공 시 success")
//    public ResponseEntity<?> exit(HttpServletRequest request) {
//    	String jwtToken = request.getHeader("auth-token");
//    	
//    	try {
//	    	String accessToken = (String) jwtUtil.extractAllClaims(jwtToken).get("accessToken");
//	    	String kakaoId = kakaoAPI.unlink(accessToken);
//	    	System.out.println("탈퇴하기: " + kakaoId);
//	    	if(kakaoId != null) {
//	    		// 어드민키로 삭제
//	    		
//	    	}
//	    	
//	    	// db에서 삭제
//	    	authService.deleteByKakaoId(kakaoId);
//	    	
//	    	return new ResponseEntity<>("success", HttpStatus.OK);
//	    	
//    	} catch (Exception e) {
//    		
//    		e.printStackTrace();
//    		return new ResponseEntity<>("fail", HttpStatus.OK);
//    	}
//    }
}