package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.RecipeAppVo;
import com.spring.dishcovery.entity.RecipeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecipeAppMapper {

    List<RecipeAppVo> getAppRecipes();

    List<RecipeVo> getAllRecipes();

    List<RecipeVo> getRankList();

    List<RecipeVo> getRoulleteData();

    List<RecipeVo> getSearchRecipes(String searchName);

    int SaveRecipeData(RecipeVo recipeVo);

    List<RecipeVo> getMyRecipes(String userId);

    int insertRecipeSteps(List<RecipeVo> stepList);

    RecipeVo getRecipeDataDetail(String recipeId);

    List<RecipeVo> selectStepList(String recipeId);

    // 유저가 이미 조회했는지 체크
    Integer checkUserView(@Param("recipeId") String recipeId, @Param("userId") String userId);
    // 조회 기록 저장
    int insertUserView(@Param("recipeId") String recipeId,  @Param("userId") String userId);

    // 레시피 조회수 증가
    int updateViewCount(@Param("recipeId") String recipeId);

}
