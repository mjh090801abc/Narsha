package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.RecipeAppVo;
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

    public List<RecipeAppVo> getAppRecipes() {
        return recipeAppMapper.getAppRecipes();
    }

    public List<RecipeVo> getAllRecipes() {
        return recipeAppMapper.getAllRecipes();
    }

    public List<RecipeVo> getRankList() {
        return recipeAppMapper.getRankList();
    }

    public RecipeVo getRankData(String rankId) {
        return recipeAppMapper.getRankData(rankId);
    }



    public List<RecipeVo> getRoulleteData() {
        return recipeAppMapper.getRoulleteData();
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

    public RecipeVo getRecipeDataDetail(String recipeId, String userId) {
        int result;

        RecipeVo recipeVo = new  RecipeVo();

       // userid가 있을때만 조회수 증가 시키기
        if (userId != null && !"".equals(userId)) {

            //1) 이 유저가 이미 조회했는지 확인
            Integer exists = recipeAppMapper.checkUserView(recipeId, userId);

            if (exists != null && exists > 0) {
                recipeVo = recipeAppMapper.getRecipeDataDetail(recipeId);
                recipeVo.setStepList(recipeAppMapper.selectStepList(recipeId));

                return recipeVo;  // 이미 조회한 유저 → 증가 X
            }
            // 2) 조회 기록 추가
            result = recipeAppMapper.insertUserView(recipeId, userId);

            // 3) 실제 조회수 증가
            result = recipeAppMapper.updateViewCount(recipeId);

        }

        recipeVo = recipeAppMapper.getRecipeDataDetail(recipeId);
        recipeVo.setStepList(recipeAppMapper.selectStepList(recipeId));

        return recipeVo;
    }


}
