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
    public String mainPage(Model model) {

        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getAllRecipes();

        model.addAttribute("recipes", recipes);

        return "mainPage";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {

        return "/recipe/register";
    }

}
