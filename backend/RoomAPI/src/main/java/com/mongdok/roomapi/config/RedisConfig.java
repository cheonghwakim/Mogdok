package com.mongdok.roomapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * author: pinest94
 * since: 2021-04-29
 */

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

//    @Bean public ObjectMapper objectMapper() {
//        var mapper = new ObjectMapper();
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModules(new JavaTimeModule(), new Jdk8Module()); return mapper;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate2(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
//        var serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//        var tpl = new RedisTemplate<String, Object>();
//        tpl.setConnectionFactory(connectionFactory);
//        tpl.setKeySerializer(new StringRedisSerializer());
//        tpl.setValueSerializer(serializer);
//        tpl.setHashKeySerializer(new StringRedisSerializer()); tpl.setHashValueSerializer(serializer);
//        return tpl;
//    }
}