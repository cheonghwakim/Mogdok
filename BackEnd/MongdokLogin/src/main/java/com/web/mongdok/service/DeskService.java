package com.web.mongdok.service;

import com.web.mongdok.dto.UserProfileDto;
import com.web.mongdok.entity.Desk;

public interface DeskService {

	Desk findByUserId(String userId);

	Desk setDesk(String userId, String promise);

	UserProfileDto findByUserName(String userName);

}
