package com.spring.dishcovery.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentEntity {

    private Long commentId;
    private Long boardId;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}