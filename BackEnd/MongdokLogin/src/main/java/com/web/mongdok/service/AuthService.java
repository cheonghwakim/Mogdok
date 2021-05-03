package com.web.mongdok.service;

import java.util.List;
import com.web.mongdok.entity.User;


public interface AuthService {

    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";

    User findByUserId(String id);
    
    List<User> findAll();

	String VerificationUser(String key);

	User findByKakaoId(String kakaoId);

	User findByEmail(String email);

	void signUpSocialUser(User user);

	boolean findByUserName(String nickname);

	User save(User user);

	boolean deleteByKakaoId(String kakaoId);
}
