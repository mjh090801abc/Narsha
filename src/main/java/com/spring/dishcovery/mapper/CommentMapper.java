package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    void insertComment(CommentEntity comment);

    List<CommentEntity> selectCommentsByBoardId(@Param("boardId") Long boardId);

    int countCommentsByBoardId(@Param("boardId") Long boardId);

    void deleteComment(@Param("commentId") Long commentId);

    void deleteCommentsByBoardId(@Param("boardId") Long boardId);
}