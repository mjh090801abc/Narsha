package com.spring.dishcovery.controller;

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

    // 내부 클래스 (요청 DTO)
    public static class DataRequest {
        private String message;
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}