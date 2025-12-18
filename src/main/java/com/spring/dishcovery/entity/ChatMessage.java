package com.spring.dishcovery.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private Long id;
    private String userId;
    private String role; // "user" or "assistant"
    private String content;
    private LocalDateTime createdAt;

}
