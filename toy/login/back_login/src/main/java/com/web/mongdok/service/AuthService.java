package com.web.mongdok.service;

import java.util.List;
import java.util.Optional;

import com.web.mongdok.dto.SignupReqDto;
import com.web.mongdok.entity.User;

import javassist.NotFoundException;

public interface AuthService {

    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";

    void signUpSocialUser(SignupReqDto member);

    Optional<User> loginSocialUser(String id) throws NotFoundException;

    User findByUserId(String id);
    
    List<User> findAll();
}
