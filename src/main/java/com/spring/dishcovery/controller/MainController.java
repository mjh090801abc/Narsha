package com.spring.dishcovery.controller;

import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    RecipeAppService service;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/MainPage")
    public String mainPage(Model model, HttpServletRequest request,
                           @CookieValue(value = "JWT_TOKEN", required = false) String token) {

        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getAllRecipes();

        // 쿠키에서 JWT 추출
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // JWT가 있고 유효하면 로그인 상태
        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.getUserIdFromToken(token);
            String userName = jwtUtil.getUserNameFromToken(token);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("userId", userId);
            model.addAttribute("userName", userName);
            model.addAttribute("recipes", recipes);
        } else {
            model.addAttribute("recipes", recipes);
            model.addAttribute("isLoggedIn", false);
        }

        return "mainPage";
    }

}
