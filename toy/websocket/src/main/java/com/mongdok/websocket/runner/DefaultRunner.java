package com.mongdok.websocket.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * author: pinest94
 * since: 2021-05-07
 */

@Component
@Slf4j
public class DefaultRunner implements ApplicationRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJrYWthb0lkIjoiMTcxMDk3MDg4OCIsImFjY2Vzc1Rva2VuIjoiQXJNV25lTzlOYzRqUlIzelNXRWlNNTM4YjFmVk9NaTB2SVMwcmdvcGNCUUF1NTg2NSwiZXhwIjo0MjEyMzU1ODY1fQ.CJoehOuUSqwplOlI4_RJygWIgW-pW3SgwbQaN87aXK9XA6ekFSrmpqE7MVQInVqNldIILgUfH4_AGVMcrKGr0A";

//        HashOperations<String, String, String> hash = redisTemplate.opsForHash();
//        Map<String, String> map = hash.entries(accessToken);

//        log.info("size : {}", map.size());
//
//        log.info("userName : {}", map.get("userName"));
//        log.info("userId : {}", map.get("userId"));
    }
}
