package com.web.mongdok.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.mongdok.entity.Desk;
import com.web.mongdok.entity.User;
import com.web.mongdok.repository.DeskRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeskServiceImpl implements DeskService {

    @Autowired
    private DeskRepository deskRepository;
    
	@Override
	public Desk setDesk(User user, String promise) {
		Desk desk = new Desk();
		desk.setUser(user);
		desk.setPromise(promise);
		
		System.out.println("desk: " + desk);
		return deskRepository.save(desk);
	}

	@Override
	public Desk findByUser(User user) {
		return deskRepository.findByUser(user);
	}


}
