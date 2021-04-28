package com.web.mongdok.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.mongdok.dto.SignupReqDto;
import com.web.mongdok.entity.User;
import com.web.mongdok.repository.KaKaoUserRepository;
import com.web.mongdok.utils.RedisUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private KaKaoUserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailService emailService;
    
	@Override
	public void signUpSocialUser(SignupReqDto user) {
		System.out.println("asdfasdfadsf");
        User newUser = new User();
        newUser.setCategory("");
        newUser.setEmail(user.getEmail());
        newUser.setNickname("");
        newUser.setUserId(user.getUserId());
        newUser.setKakaoId(user.getKakaoId());
        
        System.out.println(newUser);
        userRepository.save(newUser);
	}

	@Override
	public Optional<User> loginSocialUser(String id) throws NotFoundException {
        Optional<User> kakaoUser = userRepository.findByKakaoId(id);
        if(!kakaoUser.isPresent()) throw new NotFoundException("멤버가 조회되지 않음");
        
        return kakaoUser;
	}

	@Override
	public User findByUserId(String id) {
        User user = userRepository.findByUserId(id);
        
        return user;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

    @Override
    public String sendVerificationMail(User user) {
        String VERIFICATION_LINK = "http://localhost:8080/redis/verify/";
        if(user == null) 
        	return "멤버가 조회되지 않음";
        
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), user.getEmail(), 60 * 30L); // 저장 기한
        emailService.sendMail(user.getEmail(), "몽독이 인증메일입니다.", VERIFICATION_LINK + uuid.toString());
        
        return "success";
    }

    @Override
    public String verifyEmail(String key) {
        String userEmail = redisUtil.getData(key);
//        System.out.println("redis result: " + userEmail);
        User user = userRepository.findByEmail(userEmail);
//        System.out.println("user: " + user);
        if(user == null) 
        	return "멤버가 조회되지않음";
        
        redisUtil.deleteData(key);
        return "success";
    }

    // refresh_token이 있으면 success
	@Override
	public String VerificationUser(String key) {
		String refresh_token = redisUtil.getData(key);
		if(refresh_token != null)
			return "success";
		
		return "fail";
	}

	@Override
	public Optional<User> findByKakaoId(String kakaoId) {
		return userRepository.findByKakaoId(kakaoId);
	}




}