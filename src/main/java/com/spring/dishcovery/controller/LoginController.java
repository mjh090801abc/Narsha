package com.spring.dishcovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/dishcovery_signup")
    public String dishcovery_signup(@RequestParam String username,
                                    @RequestParam String password,
                                    Model model) {

        model.addAttribute("username", username);
        model.addAttribute("password", password);
        return "html/login";
    }


}
