package com.hindi4all.h4j.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hindi4all.h4j.dto.CodeDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CodeService {
    
    private final RedisTemplate<String, Object> redisTemplate ;

    public void writeToRedisQueue(CodeDto codeobj){

        redisTemplate.opsForList().leftPush("jobs", codeobj.toString());

    }

    // Poll redis for code execution results
    public Object pollForID(String ID) {
        // Get the results and retunrn
        return redisTemplate.opsForValue().get("job" + ID);

    }
}
