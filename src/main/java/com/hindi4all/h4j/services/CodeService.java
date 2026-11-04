package com.hindi4all.h4j.services;

import java.time.Instant;
import java.util.UUID;

import com.hindi4all.h4j.entity.ExecutionStatus;
import com.hindi4all.h4j.entity.Submission;
import com.hindi4all.h4j.repository.SubmissionRepository;
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

    private final SubmissionRepository submissionRepository;

    @Qualifier
    private final RedisTemplate<String, Object> redisStringTemplate;

    public CodeService( RedisTemplate<String, Object> redisTemplate, SubmissionRepository submissionRepository,
        @Qualifier("StringOnlyRedisTemplate") 
        RedisTemplate<String, Object> redisStringTemplate 
    ){
        
        this.redisTemplate = redisTemplate;
        this.submissionRepository = submissionRepository;

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

        // Save to database
        submissionRepository.save(
                new Submission(
                    null,
                    new UUID(8,8),
                    ExecutionStatus.EXECUTING,
                    transpiledJavaCode,
                    "null",
                    null,
                    null,
                    Instant.now(),
                    0L,
                    Instant.now(),
                    Instant.now()
        ));

        System.out.println(transpiledJavaCode);

        return ID;
         
	}

    
}
