package toy.mongsil.tutorial;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import toy.mongsil.tutorial.entity.Rest;
import toy.mongsil.tutorial.entity.User;
import toy.mongsil.tutorial.rest.repository.RestRepository;
import toy.mongsil.tutorial.user.repository.UserRepository;

@Component
public class RedisRunner implements ApplicationRunner {

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestRepository restRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set("name", "saelobi");
		values.set("framework", "spring");
		values.set("message", "hello world");

		User user = new User();
		user.setUserId("1693013");
//		user.setStudyStart(LocalDateTime.parse("15:00:00"));

		Rest rest = new Rest();
		rest.setUserId("1693013");
		rest.setRestId(0);
//		rest.setRestStart(LocalDateTime.parse("15:30:00"));
//		rest.setRestEnd(LocalDateTime.parse("16:00:00"));

		userRepository.save(user);
		restRepository.save(rest);

		Optional<User> byId = userRepository.findById(user.getUserId());
		System.out.println(byId.orElse(new User()).getUserId());

	}
}
