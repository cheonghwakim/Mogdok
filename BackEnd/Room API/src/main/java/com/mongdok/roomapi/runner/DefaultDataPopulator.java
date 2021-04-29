package com.mongdok.roomapi.runner;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.repository.SeatRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * author: pinest94
 * since: 2021-04-29
 */

@Component
public class DefaultDataPopulator implements ApplicationRunner {

    @Autowired
    SeatRedisRepository seatRedisRepository;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Seat seat = Seat.builder()
//                .id("sessionA##1")
//                .seatNo(7)
//                .userName("hansol")
//                .userId("dsDFqnif8s")
//                .sessionId("sessionA")
//                .build();
//
//        seatRedisRepository.save(seat);

//        Seat seat = seatRedisRepository.findById("sessionA##1").orElse(null);
//        System.out.println(seat.toString());
//        seat = seatRedisRepository.findByUserId("dsDFqnif8s").orElse(null);
//        System.out.println(seat.toString());
        System.out.println(redisTemplate.keys("Seats:sessionA*").size());

//        seatRedisRepository.findAll().forEach(s -> {
//            System.out.println("================================");
//            System.out.println(s.getId());
//            System.out.println(s.getSeatNo());
//            System.out.println(s.getUserId());
//            System.out.println(s.getUserName());
//            System.out.println(s.getStudyType());
//            System.out.println("================================");
//        });
    }
}
