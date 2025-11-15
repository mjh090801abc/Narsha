package com.spring.dishcovery.controller;

import com.spring.dishcovery.config.CookieUtil;
import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.entity.RecipeVo;
import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.service.RecipeAppService;
import com.spring.dishcovery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RecipeAppService service;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @GetMapping("/MainPage")
    public String mainPage(Model model) {

        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getAllRecipes();

        model.addAttribute("recipes", recipes);
        model.addAttribute("rcpClassNm", "seg-btn active");
        model.addAttribute("rankClassNm", "seg-btn");

        return "mainPage";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {

        UserEntity user = new  UserEntity();
        // JWT 쿠키에서 토큰 가져오기
        String token = cookieUtil.getTokenFromCookies(request, "JWT_TOKEN");
        String userId = jwtUtil.getUserIdFromToken(token);

        user.setUserId(userId);
        user = userService.findByUserId(userId);

        List<RecipeVo> myRecipes = new ArrayList<>();
        myRecipes = service.getMyRecipes(userId);


        model.addAttribute("user", user);
        model.addAttribute("myRecipes", myRecipes);

        return "user/MyPage";
    }

    @GetMapping("/pageGubun")
    public String pageGubun(@RequestParam String gubun, Model model, HttpServletRequest request) {


        String rcpClassNm = "";
        String rankClassNm = "";

        if("recipe".equals(gubun)) {
            rcpClassNm = "seg-btn active";
            rankClassNm = "seg-btn";
        }else{
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn active";
        }
        List<RecipeVo> recipes = new ArrayList<>();
        recipes = service.getAllRecipes();

        model.addAttribute("recipes", recipes);
        model.addAttribute("gubun", gubun);
        model.addAttribute("rcpClassNm", rcpClassNm);
        model.addAttribute("rankClassNm", rankClassNm);

        return "mainPage";
    }


}
