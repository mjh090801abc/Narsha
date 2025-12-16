package com.spring.dishcovery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {


    @GetMapping("/boardList")
    public String boardList() {

        return "board/board";
    }


    @GetMapping("/boardWrite")
    public String boardWrite() {

        return "board/board-write";
    }

}
