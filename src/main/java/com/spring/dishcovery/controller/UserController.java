package com.spring.dishcovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/dishcovery_signup")
    public String dishcoverySignup(Model model) {
        return "html/signup";
    }

    @PostMapping("/dishcovery_signup")
    public String dishcoverySignup(@RequestParam String username,
                                   @RequestParam String password,
                                   Model model) {

        model.addAttribute("username", username);
        return "html/signup";
    }


}
