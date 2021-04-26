package toy.mongsil.tutorial.user.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.stereotype.Repository;

import toy.mongsil.tutorial.entity.User;

@Repository
public class UserRepository {
	private int dbCount = 0;

	public User getUser(String userid) {
		dbCount++;
		User user = new User();

		LocalDate nowDate = LocalDate.now();//현재날짜
		LocalTime nowtime = LocalTime.now();//현재시간
	
		System.out.println(nowDate);
		System.out.println(nowtime);
	
		user.setUserId(userid);

//		user.setCreatetime(time);

		return user;
	}

	public int getDbCount() {
		return dbCount;
	}
}
