package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankController {

    private final RecipeAppService recipeAppService;

    @GetMapping("/Rank")
    public List<RecipeVo> Rank(Model model) {
        List<RecipeVo> recipes = recipeAppService.getAllRecipes();

        return recipes;
    }
}
