package com.mongdok.roomapi.repository;

import com.mongdok.roomapi.model.Seat;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * author: pinest94
 * since: 2021-04-29
 */
public interface SeatRedisRepository extends CrudRepository<Seat, String> {

    Optional<Seat> findByUserId(String userId);
}