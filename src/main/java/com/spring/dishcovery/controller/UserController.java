package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveUser(@ModelAttribute UserEntity user, Model model, RedirectAttributes redirectAttributes) {

        int result = 0;
        String gubun = "";
        result = userService.saveUserData(user);


        if (result > 0) {
            gubun = "login";
            redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
        } else {
            gubun = "signup";
            redirectAttributes.addFlashAttribute("msg", "회원가입이 실패하였습니다.");
        }

        redirectAttributes.addAttribute("gubun", gubun); // 리다이렉트할 때 담아가는 파라메타(gubun)의 그릇 역할
        return "redirect:/dishcovery_login";
    }

    @PostMapping("/userLogin")
    public String userLogin(@RequestParam String userId,
                            @RequestParam String userPswd,
                            HttpServletResponse response,
                            RedirectAttributes redirectAttributes) {

        return "redirect:/dishcovery_login";
    }

}
