package com.hindi4all.h4j.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate; 
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean 
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        // JSON serializers to avoid /x11 etc bytes in redis data
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.json());
        
        
        template.afterPropertiesSet();
      //  template.setHashSerializer(new StringRedisSerializer());
        
        return template;
    }
    
}