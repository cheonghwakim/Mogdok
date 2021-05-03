package com.web.mongdok.controller;

import java.util.Map;
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
    @ApiOperation(value = "로그인 / kakaoAPI에서 accessToken, refreshToken 발급 // 성공 시 jwtRefreshToken, 실패 시 fail")
    public ResponseEntity<?> klogin(@RequestParam @ApiParam(value = "프론트에서 전달받은 authCode") String authorizeCode) {
    	
    	try {
	    	Map<String, String> kakaoAccessToken = kakaoAPI.getAccessToken(authorizeCode);
	    	String accessToken = kakaoAccessToken.get("accessToken");
	    	String refreshToken = kakaoAccessToken.get("refreshToken");
	        Map<String, String> userInfo = kakaoAPI.getUserInfo(accessToken, refreshToken);

	        String jwtRefreshToken;
	        
	        KakaoUserDto kakaoUser = new KakaoUserDto();
	        kakaoUser.setAccessToken(accessToken);
	        kakaoUser.setRefreshToken(refreshToken);
	        kakaoUser.setKakaoId(userInfo.get("id"));
	        // 여기서 담겨줘야 되는 것들 생각해보기 (지금: accessToken, refreshToken, kakaoId)
	        jwtRefreshToken = jwtUtil.doGenerateToken(kakaoUser, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);

	        return new ResponseEntity<>(jwtRefreshToken, HttpStatus.OK);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @GetMapping("/login")
    @ApiOperation("로그인 요청 (객체가 있다면 정보 return (user), 없다면 null)")
    public ResponseEntity<?> login(HttpServletRequest request) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	
    	String kakaoId = (String) jwtUtil.extractAllClaims(jwtToken).get("kakaoId");
    	System.out.println("kakaoId: " + kakaoId);
    	
    	User user = null;
//    	if(redisUtil.getData(kakaoId) != null) {
    		user = authService.findByKakaoId(kakaoId);
//    	}
    		
		if(user != null && redisUtil.getData(kakaoId) == null) {
			redisUtil.setData(kakaoId, jwtToken);
//			userinfo.setPromise(user.getDesk().getPromise());
		}
		
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PostMapping("/nickname")
    @ApiOperation("닉네임 중복 처리 (실패 overlap 성공 success)")
    public ResponseEntity<?> nickname(@RequestBody @ApiParam(value = "유저의 닉네임") String nickname) {

    	if(authService.findByUserName(nickname))
    		return new ResponseEntity<>("overlap", HttpStatus.OK);
    	return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    @PostMapping("/signup")
    @ApiOperation("회원 가입할 때 정보 저장// user 반환")
    public ResponseEntity<?> signUp(HttpServletRequest request, @RequestBody @ApiParam(value = "회원 가입 form에서 얻은 객체") SignupDto user) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	System.out.println(jwtToken);

    	String kakaoId = (String) jwtUtil.extractAllClaims(jwtToken).get("kakaoId");
    	User newUser = new User();
    	
    	System.out.println(authService.findByKakaoId(kakaoId));
    	if(authService.findByKakaoId(kakaoId) == null) {
	    	String uuid = UUID.randomUUID().toString();
	    	
	    	BeanUtils.copyProperties(user, newUser);
	    	newUser.setId(uuid);
	    	newUser.setKakaoId(kakaoId);

	    	System.out.println(newUser);
	    	
			authService.signUpSocialUser(newUser); // 회원 가입
	        deskService.setDesk(uuid, user.getPromise()); // 내 책상 초기화
        
    	}    	
//    	// redis에 정보 저장
    	RedisUserDto redisUser = new RedisUserDto();
    	BeanUtils.copyProperties(newUser, redisUser);
    	redisUser.setPromise(user.getPromise());
    	redisUser.setAccessToken((String) jwtUtil.extractAllClaims(jwtToken).get("accessToken"));
    	redisUser.setRefreshToken((String) jwtUtil.extractAllClaims(jwtToken).get("refreshToken"));

    	System.out.println(redisUser);
    	redisUtil.setObjectExpire(jwtToken, redisUser, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
 // 실제 회원 가입 (카카오 로그인 버튼 누르고 -> 회원가입 버튼 누르면 // 회원가입, 내책상 초기화) (isNew가 O일 때만)
    @PostMapping("/mypage")
    @ApiOperation("마이페이지에서 정보 수정 // 성공: success, 실패: fail")
    public ResponseEntity<?> mypage(@RequestBody @ApiParam(value = "user 객체") User user) {
		
		if(authService.save(user) == null) // 회원 가입
			return new ResponseEntity<>("fail", HttpStatus.OK);
	
    	return new ResponseEntity<>("success", HttpStatus.OK);
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

    @GetMapping("/unlink")
    @ApiOperation("카카오 연결 끊기(탈퇴) // 성공 시 success")
    public ResponseEntity<?> exit(HttpServletRequest request) {
    	String jwtToken = request.getHeader("auth-token");
    	
    	String accessToken = (String) jwtUtil.extractAllClaims(jwtToken).get("accessToken");
    	System.out.println("토큰 갱신하기: " + kakaoAPI.unlink(accessToken));
    	// db에서 삭제
    	
		return new ResponseEntity<>("success", HttpStatus.OK);
    }
}