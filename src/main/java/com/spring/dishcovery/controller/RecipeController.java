package com.spring.dishcovery.controller;


import com.spring.dishcovery.config.CookieUtil;
import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.CodeVO;
import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.CodeService;
import com.spring.dishcovery.service.RecipeAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeAppService service;
    private final CodeService codeService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @GetMapping("/searchRecipe")
    public String searchRecipe(@RequestParam String searchName,
                               @RequestParam String gubun,
                               Model model) {

//        RecipeVo searchVo = new RecipeVo();
//        searchVo.setSearchName(searchName);

        List<RecipeVo> recipes = new ArrayList<>();

        String rcpClassNm = "";
        String rankClassNm = "";
        String lastClassNm = "";
        String url = "";

        RecipeVo fstVo = new RecipeVo();
        RecipeVo sndVo = new RecipeVo();
        RecipeVo trdVo = new RecipeVo();

        if("recipe".equals(gubun)) {
            rcpClassNm = "seg-btn active";
            rankClassNm = "seg-btn";
            lastClassNm = "seg-btn";

            recipes = service.getSearchRecipes(searchName);
            url = "/mainPage";

        }else if("rank".equals(gubun)){
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn active";
            lastClassNm = "seg-btn";

            // recipes = service.getRankList();
            fstVo = service.getRankData("1");
            sndVo = service.getRankData("2");
            trdVo = service.getRankData("3");

            url = "recipe/RankPage";

        }else{
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn";
            lastClassNm = "seg-btn active";

            recipes = service.getRoulleteData();
            url = "recipe/RoulettePage";
        }


        model.addAttribute("recipes", recipes);
        model.addAttribute("searchName", searchName);
        model.addAttribute("gubun", gubun);
        model.addAttribute("rcpClassNm", rcpClassNm);
        model.addAttribute("rankClassNm", rankClassNm);
        model.addAttribute("lastClassNm", lastClassNm);
        model.addAttribute("fstVo", fstVo);
        model.addAttribute("sndVo", sndVo);
        model.addAttribute("trdVo", trdVo);

        return url;
    }


    @GetMapping("/recipeWrite")
    public String recipeWrite(Model model, HttpServletRequest request) {

        List<CodeVO> categoryList = codeService.codeList("CTG");
        List<CodeVO> levelList = codeService.codeList("LV");


        model.addAttribute("categoryList", categoryList);
        model.addAttribute("levelList", levelList);

        return "recipe/RecipeReg";
    }

    @PostMapping("/SaveRecipeData")
    public String saveRecipeData(@ModelAttribute RecipeVo recipeVo, HttpServletRequest request) {

        int result = 0;

        String token = cookieUtil.getTokenFromCookies(request, "JWT_TOKEN");
        String userId = jwtUtil.getUserIdFromToken(token);

        recipeVo.setUserId(userId);

        result = service.SaveRecipeData(recipeVo);

        return "redirect:/MainPage";
    }

    @GetMapping("/recipe/detail")
    public String recipeDetail(@RequestParam String recipeId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        RecipeVo recipe = new RecipeVo();
        String userId ="";
        String gubun ="login";
        String token = cookieUtil.getTokenFromCookies(request, "JWT_TOKEN");

        if(token != null) {
            userId = jwtUtil.getUserIdFromToken(token);
        }else{
            redirectAttributes.addFlashAttribute("msg", "로그인이 필요한 서비스입니다.");
            redirectAttributes.addAttribute("gubun", gubun);

            return "redirect:/dishcovery_login";
        }

        List<RecipeVo> stepList = new ArrayList<>();

        List<CodeVO> categoryList = codeService.codeList("CTG");
        List<CodeVO> levelList = codeService.codeList("LV");

        recipe = service.getRecipeDataDetail(recipeId,userId);
        stepList = recipe.getStepList();

        model.addAttribute("recipe", recipe);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("stepList", stepList);
        model.addAttribute("levelList",levelList);

        return "recipe/RecipeDetail";

    }

}
