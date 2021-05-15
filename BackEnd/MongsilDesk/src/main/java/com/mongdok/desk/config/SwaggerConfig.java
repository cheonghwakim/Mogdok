package com.mongdok.desk.config;

import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		List<Parameter> global = new ArrayList<>();
		global.add(new ParameterBuilder()//헤더 auth token 전역에서 사용
				.name("auth-token")
				.description("auth-token")
				.parameterType("header")
				.required(false)
				.modelRef(new ModelRef("string"))
				.build());
		
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(global)
				.select()
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				.paths(PathSelectors.any())
				.build();

	}
}
