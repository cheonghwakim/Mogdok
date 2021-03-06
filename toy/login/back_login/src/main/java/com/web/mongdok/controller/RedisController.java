package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.entity.User;
import com.web.mongdok.service.AuthService;


@RestController
@RequestMapping("redis")
public class RedisController {

	@Value("${CF_INSTANCE_IP:127.0.0.1}") 
	private String ip;
	
	@Autowired
	KakaoLoginController kloginController;
	
    @Autowired
    private AuthService authService;  
    
	@GetMapping("/test") 
	public ResponseEntity<?> redisTest(HttpSession session) { 
		
		try {
			UUID uid = Optional.ofNullable(UUID.class.cast(session.getAttribute("uid")))
						.orElse(UUID.randomUUID()); session.setAttribute("uid", uid); 
			
			Map m = new HashMap<>(); 
			m.put("instance_ip", this.ip); 
			m.put("uuid", uid.toString()); 
		
			return new ResponseEntity<>(m, HttpStatus.OK); 
		
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED); 
		}
	}
	
	// user auth (key: 쿠키에 저장된 refresh_token)
	@PostMapping("/userVerify")
    public ResponseEntity<?> userVerify(@RequestParam String key, HttpServletResponse res) {
		
		try {
        	
            if(authService.VerificationUser(key).equals("success"))
            	return new ResponseEntity<>("exist", HttpStatus.OK);
        
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>("unknown", HttpStatus.OK); // 이러면 다시 로그인 해야 함
    }
}