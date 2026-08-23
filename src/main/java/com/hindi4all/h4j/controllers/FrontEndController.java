package com.hindi4all.h4j.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontEndController {

    @GetMapping("/eee")
    public String getHomePage(){
        return "index.html";
    }

    
}
