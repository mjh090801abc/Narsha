package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.mapper.RecipeAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeAppService {

    @Autowired
    RecipeAppMapper recipeAppMapper;

    public List<RecipeVo> getAllRecipes() {
        return recipeAppMapper.getAllRecipes();
    }

    public List<RecipeVo> getSearchRecipes(String searchName) {
        return recipeAppMapper.getSearchRecipes(searchName);
    }


    public int SaveRecipeData(RecipeVo recipeVo) {
        int result = 0;
        return recipeAppMapper.SaveRecipeData(recipeVo);
    }
}
