package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    RecipeAppService service;

    @GetMapping("/MainPage")
    public String mainPage(Model model) {

        List<RecipeVo> recipes = new ArrayList<>();

        recipes = service.getAllRecipes();

        model.addAttribute("recipes", recipes);

        return "mainPage";
    }

}
