package com.spring.dishcovery.controller;

import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import com.spring.dishcovery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RecipeAppService service;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/MainPage")
    public String mainPage(Model model) {

        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getAllRecipes();

        model.addAttribute("recipes", recipes);

        return "mainPage";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {

        return "recipe/RecipeReg";
    }


}
