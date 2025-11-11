package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.RecipeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeAppMapper {

    List<RecipeVo> getAllRecipes();

    List<RecipeVo> getSearchRecipes(String searchName);

    int SaveRecipeData(RecipeVo recipeVo);

    List<RecipeVo> getMyRecipes(String userId);

}
