package com.mongdok.websocket.config;

import com.mongdok.websocket.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * author: pinest94
 * since: 2021-05-07
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .formLogin().disable()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
