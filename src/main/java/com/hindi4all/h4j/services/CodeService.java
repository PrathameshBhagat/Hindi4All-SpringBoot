package com.hindi4all.h4j.services;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hindi4all.h4j.dto.CodeDto;
import com.hindi4all.h4j.dto.CodeSubmitDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CodeService {
    
    private final RedisTemplate<String, Object> redisTemplate ;

    private RestClient restClient;

    public void writeToRedisQueue(CodeDto codeobj){

        redisTemplate.opsForList().leftPush("jobs", codeobj.toString());

    }

    // Poll redis for code execution results
    public Object pollForID(String ID) {
        // Get the results and retunrn
        return redisTemplate.opsForValue().get("job" + ID);

    }

	public UUID handleCode(CodeSubmitDto code) {

        UUID ID = UUID.randomUUID();

        restClient = RestClient.create();
 
        String transpiledJavaCode = restClient.post()
        .uri("http://localhost/decode.php?comp=online&lang=java")
        .contentType(MediaType.TEXT_PLAIN)
        .body(code.getCode())
        .retrieve()
        .body(String.class);

        System.out.println(transpiledJavaCode);

        return ID;
         
	}

    
}
