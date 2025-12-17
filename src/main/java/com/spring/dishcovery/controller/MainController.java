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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("lastClassNm", "seg-btn");
        model.addAttribute("aiClassNm", "seg-btn");

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


        List<UserEntity> userList = new ArrayList<>();
        userList = userService.findRecommUser(userId);

//        List<RecipeVo> edit_profile = new ArrayList<>();
//        edit_profile = service.getMyRecipes(userId);
//        edit_profile = service.getMyRecipes(user.getUserPswd());



        model.addAttribute("user", user);
        model.addAttribute("myRecipes", myRecipes);
        model.addAttribute("userList", userList);

        return "user/MyPage";
    }

    @GetMapping("/pageGubun")
    public String pageGubun(@RequestParam String gubun, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<RecipeVo> recipes = new ArrayList<>();
        List<RecipeVo> rankList = new ArrayList<>();

        String rcpClassNm = "";
        String rankClassNm = "";
        String lastClassNm = "";
        String aiClassNm = "";
        String url = "";

        RecipeVo fstVo = new RecipeVo();
        RecipeVo sndVo = new RecipeVo();
        RecipeVo trdVo = new RecipeVo();

       String token = cookieUtil.getTokenFromCookies(request, "JWT_TOKEN");

        if("recipe".equals(gubun)) {
            rcpClassNm = "seg-btn active";
            rankClassNm = "seg-btn";
            lastClassNm = "seg-btn";
            aiClassNm = "seg-btn";

            recipes = service.getAllRecipes();
            url = "/mainPage";

        }else if("rank".equals(gubun)){
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn active";
            lastClassNm = "seg-btn";
            aiClassNm = "seg-btn";

           // recipes = service.getRankList();
            fstVo = service.getRankData("1");
            sndVo = service.getRankData("2");
            trdVo = service.getRankData("3");

            rankList = service.selectRankListSnd();

            url = "recipe/RankPage";

        }else if("last".equals(gubun)){
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn";
            lastClassNm = "seg-btn active";
            aiClassNm = "seg-btn";

            recipes = service.getRoulleteData();
            url = "recipe/RoulettePage";
        }else{
            rcpClassNm = "seg-btn";
            rankClassNm = "seg-btn";
            lastClassNm = "seg-btn";
            aiClassNm = "seg-btn active";

            if(token != null) {
                url = "recipe/AiChatPage";
            }else{

                gubun = "login";
                redirectAttributes.addFlashAttribute("loginMessage", "로그인이 필요한 서비스입니다.");
                redirectAttributes.addAttribute("gubun", gubun);

                return "redirect:/dishcovery_login";
            }

        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("gubun", gubun);
        model.addAttribute("rcpClassNm", rcpClassNm);
        model.addAttribute("rankClassNm", rankClassNm);
        model.addAttribute("lastClassNm", lastClassNm);
        model.addAttribute("aiClassNm", aiClassNm);
        model.addAttribute("fstVo", fstVo);
        model.addAttribute("sndVo", sndVo);
        model.addAttribute("trdVo", trdVo);
        model.addAttribute("rankList", rankList);

        return url;
    }


}
