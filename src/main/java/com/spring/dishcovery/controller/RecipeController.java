package com.spring.dishcovery.controller;


import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeAppService service;

    @GetMapping("/searchRecipe")
    public String searchRecipe(@RequestParam String searchName,
                               Model model) {

//        RecipeVo searchVo = new RecipeVo();
//        searchVo.setSearchName(searchName);

        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getSearchRecipes(searchName);


        model.addAttribute("recipes", recipes);
        model.addAttribute("searchName", searchName);

        return "/mainPage";
    }

    @PostMapping("/SaveRecipeData")
    public String saveRecipeData(@ModelAttribute RecipeVo recipeVo) {

        int result = 0;
        result = service.SaveRecipeData(recipeVo);

        return "redirect:/RecipeReg";
    }
}
