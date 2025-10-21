package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeAppController {

    @Autowired
    RecipeAppService recipeAppService;

    @GetMapping
    public List<RecipeVo> getRecipes() {

        return recipeAppService.getAllRecipes();
    }

}
