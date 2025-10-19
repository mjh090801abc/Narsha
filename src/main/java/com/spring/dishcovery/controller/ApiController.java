package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.DataRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot! test완료";
    }

    @PostMapping("/data")
    public String receiveData(@RequestBody DataRequest request) {
        return "Received: " + request.getMessage();
    }


}