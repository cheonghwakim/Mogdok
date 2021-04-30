package com.web.mongdok.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
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
    
	@Override
	public void signUpSocialUser(User user) {
        userRepository.save(user);
	}

	@Override
	public Optional<User> loginSocialUser(String id) throws NotFoundException {
        Optional<User> kakaoUser = userRepository.findByKakaoId(id);
        if(!kakaoUser.isPresent()) throw new NotFoundException("멤버가 조회되지 않음");
        
        return kakaoUser;
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
	public Optional<User> findByKakaoId(String kakaoId) {
		return userRepository.findByKakaoId(kakaoId);
	}

}