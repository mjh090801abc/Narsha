package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {


    // 채팅 메시지 한 줄 저장 (INSERT)
    void insert(ChatMessage chat);

    // userId 기준으로 채팅 내역 전체 가져오기 (SELECT)
    List<ChatMessage> findByUserId(@Param("userId") String userId);
}
