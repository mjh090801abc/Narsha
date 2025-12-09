package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RecipeAppService recipeAppService;

    @GetMapping("/Rank")
    public String Rank(Model model) {
        List<RecipeVo> recipes = recipeAppService.getAllRecipes();

        model.addAttribute("recipes", recipes);

        return "recipe/RankPage";


    }
}
