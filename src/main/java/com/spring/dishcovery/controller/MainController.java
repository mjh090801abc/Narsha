package com.spring.dishcovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/MainPage")
    public String mainPage() {

        return "mainPage";
    }

}
