package com.hindi4all.h4j.services;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hindi4all.h4j.dto.CodeDto;
import com.hindi4all.h4j.dto.CodeExecutedResponseDTO;
import com.hindi4all.h4j.dto.CodeSubmitDto;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class CodeService {
    
    private final RedisTemplate<String, Object> redisTemplate;

    @Qualifier
    private final RedisTemplate<String, Object> redisStringTemplate;

    public CodeService( RedisTemplate<String, Object> redisTemplate,
        @Qualifier("StringOnlyRedisTemplate") 
        RedisTemplate<String, Object> redisStringTemplate 
    ){
        
        this.redisTemplate = redisTemplate;

        // Specifically get string template
        this.redisStringTemplate = redisStringTemplate;

    }
    private RestClient restClient;

    public void writeToRedisQueue(CodeDto codeobj){

        redisTemplate.opsForList().leftPush("jobs", codeobj.toString());

    }

    // Poll redis for code execution results
    public Object pollForID(String ID) {
        // Get the results and retunrn
        String s = (String) redisStringTemplate.opsForValue().get("job:" + ID);
        if(s==null)

            return null;
System.out.println(s);
        return new CodeExecutedResponseDTO("COMPLETED", s);

    }

	public UUID handleCode(CodeSubmitDto code) {

        UUID ID = UUID.randomUUID();

        restClient = RestClient.create();
 
        String transpiledJavaCode = restClient.post()
        .uri("http://localhost/decode.php?comp=online&lang=java")
        .contentType(MediaType.valueOf("text/plain;charset=utf-8"))
        .body(code.getCode())
        .accept(MediaType.valueOf("text/plain;charset=utf-8"))
        .retrieve()
        .body(String.class);

        CodeDto javaCode = new CodeDto(ID.toString(), transpiledJavaCode);

        redisTemplate.opsForList().leftPush("jobs", javaCode);
        
        System.out.println("=>" + redisTemplate.opsForList().leftPop("jobs") );
        redisTemplate.opsForList().leftPush("jobs", javaCode);
        redisTemplate.opsForValue().set("job-status:" + ID.toString(), "PROCESSING");

        System.out.println(transpiledJavaCode);

        return ID;
         
	}

    
}
