package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.mapper.RecipeAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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


        int result;

        // recipeId 생성
        String dataPart = new SimpleDateFormat("yyMMdd").format(new Date());
        int randomPart = new Random().nextInt(900) + 100;
        String recipeId = "RCP" + dataPart + randomPart;

        recipeVo.setRecipeId(recipeId);

        //recipe_mater data insert
        result =  recipeAppMapper.SaveRecipeData(recipeVo);


        return result;
    }



    public List<RecipeVo> getMyRecipes(String userId) {
        return recipeAppMapper.getMyRecipes(userId);
    }


}
