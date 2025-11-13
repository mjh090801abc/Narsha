package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.mapper.RecipeAppMapper;
import com.spring.dishcovery.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class RecipeAppService {

    @Value("${file.upload-dir}")
    private String uploadDir;

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
        //img url 가져오기
        List<String> mainImagePaths = FileUploadUtil.saveRecipeFiles(recipeVo.getMainImages(), uploadDir, recipeId);
        recipeVo.setImgUrl(mainImagePaths.get(0));

        //recipe_mater data insert
        result =  recipeAppMapper.SaveRecipeData(recipeVo);

        //step저장
        List<RecipeVo> stepList = new ArrayList<>();
        if(recipeVo.getStepDescriptions().length > 0) {
           for(int i=0;i<recipeVo.getStepDescriptions().length;i++) {
               RecipeVo steps = new RecipeVo();
               steps.setRecipeId(recipeId);
               steps.setStepOrder(i + 1);
               steps.setStepDescription(recipeVo.getStepDescriptions()[i]);
               stepList.add(steps);
           }

            if (!stepList.isEmpty()) {
                result = recipeAppMapper.insertRecipeSteps(stepList);
            }
        }

        return result;
    }



    public List<RecipeVo> getMyRecipes(String userId) {
        return recipeAppMapper.getMyRecipes(userId);
    }


}
