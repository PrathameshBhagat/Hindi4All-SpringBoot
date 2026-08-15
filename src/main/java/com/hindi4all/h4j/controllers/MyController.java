package com.hindi4all.h4j.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hindi4all.h4j.dto.CodeDto;
import com.hindi4all.h4j.services.CodeService;

import lombok.AllArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping; 


import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * RestController
 */
@RestController
@AllArgsConstructor
public class MyController {

    private final CodeService codeService;

    private final RedisTemplate<String, Object> redisTemplate ;

    @GetMapping("/wtr")
    public String getMethodName() {

        CodeDto c = new CodeDto();
        c.setID("123456789");
        c.setCode("\n" + "public class Main {\n" +
                        "\n" + 
                        "    public static void main(String[] args) {\n" + 
                        "        System.out.println(\"hello\");\n" + 
                        "    }\n }");

        codeService.writeToRedisQueue(c);

        return new String("Hello");
    }  
    
}
