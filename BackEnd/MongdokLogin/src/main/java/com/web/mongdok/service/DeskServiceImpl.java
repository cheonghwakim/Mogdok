package com.web.mongdok.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.mongdok.dto.UserProfileDto;
import com.web.mongdok.entity.Desk;
import com.web.mongdok.repository.DeskRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeskServiceImpl implements DeskService {

    @Autowired
    private DeskRepository deskRepository;
    
	@Override
	public Desk setDesk(String userId, String promise) {
		Desk desk = new Desk();
		desk.setUserId(userId);
		desk.setPromise(promise);
		
		System.out.println("desk: " + desk);
		return deskRepository.save(desk);
	}

	@Override
	public Desk findByUserId(String userId) {
		return deskRepository.findByUserId(userId);
	}

	@Override
	public UserProfileDto findByUserName(String userName) {
		return deskRepository.findByUserName(userName);
	}


}
