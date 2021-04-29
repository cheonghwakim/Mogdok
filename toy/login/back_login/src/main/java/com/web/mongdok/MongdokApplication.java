package com.web.mongdok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MongdokApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongdokApplication.class, args);
	}

}
