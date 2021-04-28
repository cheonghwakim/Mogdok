package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongdok.dto.SignupReqDto;
import com.web.mongdok.entity.User;
import com.web.mongdok.service.AuthService;


@RestController
@RequestMapping("redis")
public class RedisController {

	@Value("${CF_INSTANCE_IP:127.0.0.1}") 
	private String ip;
	
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
	
	// redis 인증 예제
	@PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email) {
        try {
            User user = authService.findByEmail(email);
            if(authService.sendVerificationMail(user).equals("success"))
            	return new ResponseEntity<>("success", HttpStatus.OK);
        
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>("unknown", HttpStatus.OK);
    }

    @GetMapping("/verify/{key}")
    public ResponseEntity<?> getVerify(@PathVariable String key) {
    	
        try {

            if(authService.verifyEmail(key).equals("success"))
            	return new ResponseEntity<>("인증 완료", HttpStatus.OK);
            else
            	return new ResponseEntity<>("null", HttpStatus.OK);
            
        } catch (Exception e) {
        	e.printStackTrace();
        	return new ResponseEntity<>("fail", HttpStatus.UNAUTHORIZED);
        }
    }
	
	
	
	
	
	
	
	
	

//    private final RedisTemplate<String, String> redisTemplate;
//
//    public RedisController(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @PostMapping("/redis")
//    public String addRedisKey(@RequestParam String key, @RequestParam String value) {
//        System.out.println(key);
//        ValueOperations<String, String> vop = redisTemplate.opsForValue();
//        System.out.println(value);
//        vop.set(key, value);
//        
//		return key;
//    }
//
//    @GetMapping("/{key}")
//    public String getRedisKey(@PathVariable String key) {
//        ValueOperations<String, String> vop = redisTemplate.opsForValue();
//	
//    	return vop.get(key);
//    }
}