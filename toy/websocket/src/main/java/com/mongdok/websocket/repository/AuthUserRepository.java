package com.mongdok.websocket.repository;

import com.mongdok.websocket.model.AuthUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * author: pinest94
 * since: 2021-05-07
 */

@Repository
public interface AuthUserRepository extends CrudRepository<AuthUser, String> {
}
