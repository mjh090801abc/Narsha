package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    // 로그인
    @GetMapping("/dishcovery_login")
    public String dishcoveryLogin(@RequestParam String gubun, Model model) {

        String loginTab = "";
        String signUpTab = "";
        String loginPanel = "";
        String signUpPanel = "";

        if (gubun.equals("login")) {

            loginTab = "tab active";
            signUpTab = "tab";

            loginPanel = "panel show";
            signUpPanel = "panel";

        }else{
            loginTab = "tab";
            signUpTab = "tab active";

            loginPanel = "panel";
            signUpPanel = "panel show";
        }

        model.addAttribute("loginTab", loginTab);
        model.addAttribute("signUpTab", signUpTab);
        model.addAttribute("loginPanel", loginPanel);
        model.addAttribute("signUpPanel", signUpPanel);

        return "user/login";
    }
    @PostMapping("/save_user")
    public String saveUser(@ModelAttribute UserEntity user, Model model) {

        int result = 0;
        String gubun = "";
        result = userService.saveUserData(user);


        if (result > 0) {

            gubun = "login";
        } else {
            gubun = "signup";
        }

        model.addAttribute("gubun", gubun);

        return "redirect:/dishcovery_login";
    }

}
