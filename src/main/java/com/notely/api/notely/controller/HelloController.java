package com.notely.api.notely.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class HelloController {


    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World!";
    }
    
}
