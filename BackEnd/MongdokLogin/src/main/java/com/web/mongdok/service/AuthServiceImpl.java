package com.web.mongdok.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.mongdok.entity.User;
import com.web.mongdok.repository.KaKaoUserRepository;
import com.web.mongdok.utils.RedisUtil;

import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private KaKaoUserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;
    
	@Override
	public void signUpSocialUser(User user) {
        userRepository.save(user);
	}

	@Override
	public User findByUserId(String id) {
        User user = userRepository.findById(id);
        
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

    // refresh_token이 있으면 success
	@Override
	public String VerificationUser(String key) {
		String refresh_token = redisUtil.getData(key);
		if(refresh_token != null)
			return "success";
		
		return "fail";
	}

	@Override
	public User findByKakaoId(String kakaoId) {
		return userRepository.findByKakaoId(kakaoId);
	}

	@Override
	public boolean findByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

}