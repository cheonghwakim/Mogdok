package toy.mongsil.tutorial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import toy.mongsil.tutorial.entity.User;
import toy.mongsil.tutorial.user.repository.UserRepository;
@Service
public class UserService {
	@Autowired
	UserRepository repository;

	@Cacheable(key = "#userid", value = "getUser")
	public User getUser(String userid) {
		return repository.getUser(userid);
	}

	public int getDbCount() {
		return repository.getDbCount();
	}
}
