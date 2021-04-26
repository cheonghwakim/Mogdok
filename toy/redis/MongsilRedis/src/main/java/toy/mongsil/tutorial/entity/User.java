package toy.mongsil.tutorial.entity;


import java.util.Date;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash("user") //해당 클래스의 인스턴스가 레디스에 적재될 때 @RedisHash의 인수를 키로 해당 인스턴스를 값으로 적재되도록 합니다.
public class User {
	
	@Id
	private String userId;
	
	private String nickname;
	
	private String email;
	
	private String category;
	
	private String kakaoId;
	
	private Date createtime;
}
