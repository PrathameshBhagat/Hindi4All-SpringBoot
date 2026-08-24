package com.hindi4all.h4j.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hindi4all.h4j.dto.CodeDto;
import com.hindi4all.h4j.dto.CodeSubmitDto;
import com.hindi4all.h4j.services.CodeService;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/poll/{ID}")
    public ResponseEntity pollForSuccessfulJobByID(@PathVariable String ID){

        Object o  = codeService.pollForID(ID);

        if( o != null ){
            return ResponseEntity.ok(o);
        } 

        return ResponseEntity.ok()
                    .body("{ \"status\" : \"your code can be processing or rejected please retry\" }");

    }

    @PostMapping("/submitCode")
    public String processCode(@RequestBody CodeSubmitDto code){

        UUID ID = codeService.handleCode(code);

        if( ID != null)

            return ID.toString();

        throw new RuntimeException("Error Processing the code");

    }
    
}
