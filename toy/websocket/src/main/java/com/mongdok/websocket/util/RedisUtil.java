//package com.mongdok.websocket.util;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongdok.websocket.model.AuthUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
///**
// * author: pinest94
// * since: 2021-05-07
// */
//
//@RequiredArgsConstructor
//@Service
//public class RedisUtil {
//
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    public String getData(String key){
//        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
//        return valueOperations.get(key);
//    }
//
//    public AuthUser getUser(String token) {
//        String userInfo = getData(token);
//
//        AuthUser authUser = null;
//        if(userInfo != null) {
//            try {
//                authUser = objectMapper.readValue(userInfo, AuthUser.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return authUser;
//    }
//}
