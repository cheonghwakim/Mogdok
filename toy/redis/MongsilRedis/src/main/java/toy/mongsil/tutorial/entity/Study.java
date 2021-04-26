package toy.mongsil.tutorial.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash("study")
public class Study {
	@Id
	private int StudyId;

	private String UserId;

	private Date studytime;

}
