package com.web.mongdok;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.web.mongdok.interceptor.JwtInterceptor;

@Configuration
public class JwtLoginApplication implements WebMvcConfigurer {
	
	@Autowired
	private JwtInterceptor jwtInterceptor;
	
	// 인터셉터가 작동할 주소 (로그인 필요한 주소)
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).addPathPatterns("/*")	
				.excludePathPatterns(Arrays.asList("/path", "/path")); // 적용 제외 경로
	}
	
	// interceptor를 이용해서 처리하므로 전역에 Cross origin 처리 해준다.
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("auth-token");
	}
}
