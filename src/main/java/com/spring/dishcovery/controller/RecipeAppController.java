package com.spring.dishcovery.controller;

import com.spring.dishcovery.config.CookieUtil;
import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.RecipeAppVo;
import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.service.RecipeAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecipeAppController {

    private final RecipeAppService recipeAppService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @GetMapping("/getAppRecipes")
    public List<RecipeAppVo> getRecipes() {
        return recipeAppService.getAppRecipes();
    }

    @GetMapping("/getRecipes")
    public List<RecipeVo> getAppRecipes() {
        return recipeAppService.getAllRecipes();
    }

    @PostMapping("/SaveRecipeData")
    public int SaveRecipeData(HttpServletRequest request
                             ,@ModelAttribute RecipeVo recipe
                             ,@RequestHeader("Authorization") String authHeader) throws Exception {


        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdFromToken(token); // JWT에서 사용자 식별

        recipe.setUserId(userId);

        return recipeAppService.SaveRecipeData(recipe);
    }
}
