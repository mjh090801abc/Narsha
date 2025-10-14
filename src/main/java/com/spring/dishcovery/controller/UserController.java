package com.spring.dishcovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // 로그인
    @GetMapping("/dishcovery_login")
    public String dishcoveryLogin(@RequestParam String gubun, Model model) {

        String loginTab = "";
        String signUpTab = "";

        if (gubun.equals("login")) {

            loginTab = "tab active";
            signUpTab = "tab";
        }else{
            loginTab = "tab";
            signUpTab = "tab active";
        }

        model.addAttribute("loginTab", loginTab);
        model.addAttribute("signUpTab", signUpTab);

        return "html/login";
    }
}
