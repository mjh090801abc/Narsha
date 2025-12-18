package com.spring.dishcovery.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardEntity {

    private Long boardId;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private Integer viewCount;  // 조회수 추가

}