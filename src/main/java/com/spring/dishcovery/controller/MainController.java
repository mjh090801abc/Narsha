package com.spring.dishcovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/mainPage")
    public String mainPage() {

        return "html/mainPage";
    }


}
