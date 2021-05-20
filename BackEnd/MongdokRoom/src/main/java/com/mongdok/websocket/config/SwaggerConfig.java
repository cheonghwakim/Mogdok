package com.mongdok.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * author: pinest94
 * since: 2021-05-10
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                groupName("public-api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mongdok.websocket.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Mongdok Room API")
                .description("<h3>몽실이네 독서실</h3>Swagger를 이용한 Room API")
                .contact(new Contact("몽실이제자들", "https://edu.ssafy.com", "ssafy@ssafy.com")).license("SSAFY License")
                .licenseUrl("https://www.ssafy.com/ksp/jsp/swp/etc/swpPrivacy.jsp").version("1.0").build();
    }
}