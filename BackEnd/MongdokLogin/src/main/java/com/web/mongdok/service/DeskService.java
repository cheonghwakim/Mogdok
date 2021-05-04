package com.web.mongdok.service;

import com.web.mongdok.entity.Desk;
import com.web.mongdok.entity.User;

public interface DeskService {

	Desk findByUser(User user);

	Desk setDesk(User user, String promise);

}
