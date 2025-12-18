package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.BoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    void insertBoard(BoardEntity board);

    List<BoardEntity> selectBoardList();

    BoardEntity selectBoardById(@Param("boardId") Long boardId);

    void updateBoard(BoardEntity board);

    void deleteBoard(@Param("boardId") Long boardId);

    void increaseViewCount(@Param("boardId") Long boardId);
}