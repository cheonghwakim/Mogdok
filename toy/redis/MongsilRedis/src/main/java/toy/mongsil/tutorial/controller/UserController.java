package toy.mongsil.tutorial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import toy.mongsil.tutorial.entity.User;
import toy.mongsil.tutorial.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService service;
	
	@GetMapping("user")
	public User getUser(String userid) {
		return service.getUser(userid);
	}

	@GetMapping("count")
	public int count() {
		return service.getDbCount();
	}
}
