package com.web.mongdok.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class RedisController {

	@Value("${CF_INSTANCE_IP:127.0.0.1}") 
	private String ip;
	
	@GetMapping("/test/redis") 
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