package com.mongdok.websocket;

import com.mongdok.websocket.model.AuthUser;
import com.mongdok.websocket.repository.AuthUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebsocketApplicationTests {

    @Autowired
    AuthUserRepository authUserRepository;

    @Test
    void contextLoads() {

    }

//    @Test
//    void authUserTest() {
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJrYWthb0lkIjoiMTcxMDk3MDg4OCIsImFjY2Vzc1Rva2VuIjoiQXJNV25lTzlOYzRqUlIzelNXRWlNNTM4YjFmVk9NaTB2SVMwcmdvcGNCUUFBQUY1Ukx4Y0hnIiwic3ViIjoidXNlciIsImlhdCI6MTYyMDM1NTg2NSwiZXhwIjo0MjEyMzU1ODY1fQ.CJoehOuUSqwplOlI4_RJygWIgW-pW3SgwbQaN87aXK9XA6ekFSrmpqE7MVQInVqNldIILgUfH4_AGVMcrKGr0A";
//        AuthUser authUser = authUserRepository.findById(token).orElse(null);
//
//        System.out.println(authUser.toString());
//    }
}
