package com.spring.dishcovery.controller;

import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

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
            redirectAttributes.addAttribute("gubun", gubun); // 리다이렉트할 때 담아가는 파라메타(gubun)의 그릇 역할
            redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            return "redirect:/dishcovery_login";
        } else {
            gubun = "signup";
            redirectAttributes.addAttribute("gubun", gubun); // 리다이렉트할 때 담아가는 파라메타(gubun)의 그릇 역할
            redirectAttributes.addFlashAttribute("msg", "회원가입이 실패하였습니다.");
            return "redirect:/dishcovery_login";
        }
    }

    @PostMapping("/userLogin")
    public String userLogin(@RequestParam String userId,
                            @RequestParam String userPswd,
                            HttpServletResponse response,
                            RedirectAttributes redirectAttributes) {

        UserEntity user = userService.getUserData(userId, userPswd);
        if (user != null) {
            // JWT 발급
            String token = jwtUtil.generateToken(user.getUserId(), user.getUserName());

            // JWT를 HttpOnly 쿠키로 브라우저에 저장
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/"); // 전체경로 사용가능
            jwtCookie.setMaxAge(3600); // 1시간
            response.addCookie(jwtCookie);

            redirectAttributes.addFlashAttribute("msg", user.getUserName()+" 님 환영합니다.");
            return "redirect:/MainPage";
        }else
            redirectAttributes.addAttribute("gubun", "login");
        redirectAttributes.addFlashAttribute("msg", "아이디 또는 비밀번호 화인해 주세요.");
        return "redirect:/dishcovery_login";

    }

    @GetMapping("/profileEdit")
    public String profileEdit(@RequestParam String userId, Model model) {

        UserEntity user = userService.findByUserId(userId);


        model.addAttribute("user", user);

        return "/user/profileEdit";

    }
/*
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute UserEntity user, RedirectAttributes redirectAttributes) {

        boolean pwResult = userService.changePassword(user);

        if (!pwResult) {
            redirectAttributes.addFlashAttribute("error", "현재 비밀번호가 올바르지 않습니다.");
            redirectAttributes.addAttribute("userId", user.getUserId());
            return "redirect:/profileEdit";
        }else{
            redirectAttributes.addFlashAttribute("success", "회원정보가 변경되었습니다.");
            redirectAttributes.addAttribute("userName", user.getUserName());
            return "redirect:/myPage";
        }

    }
*/
    @PostMapping("/updateProfile")
    public String updateProfile(
            @RequestParam String userId,
            @RequestParam String userMail,
            @RequestParam String userName,
            @RequestParam(required = false) MultipartFile profileImg,
            @RequestParam(required = false) String currentPw,
            @RequestParam(required = false) String newPw,
            RedirectAttributes redirectAttributes
    ) {

        try {
            userService.updateProfile(
                    userId,
                    userMail,
                    userName,
                    profileImg,
                    currentPw,
                    newPw
            );
            redirectAttributes.addFlashAttribute("success", "프로필이 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profileEdit?userId=" + userId;
    }


}
