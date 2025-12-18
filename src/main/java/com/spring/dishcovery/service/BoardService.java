package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.BoardEntity;
import com.spring.dishcovery.entity.CommentEntity;
import com.spring.dishcovery.mapper.BoardMapper;
import com.spring.dishcovery.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public void createBoard(BoardEntity board) {
        boardMapper.insertBoard(board);
    }

    public List<Map<String, Object>> getBoardList() {
        List<BoardEntity> boards = boardMapper.selectBoardList();

        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (BoardEntity board : boards) {
            Map<String, Object> map = new HashMap<>();
            map.put("board", board);
            map.put("commentCount", commentMapper.countCommentsByBoardId(board.getBoardId()));
            result.add(map);
        }

        return result;
    }

    @Transactional
    public Map<String, Object> getBoardDetail(Long boardId) {
        boardMapper.increaseViewCount(boardId);

        BoardEntity board = boardMapper.selectBoardById(boardId);
        List<CommentEntity> comments = commentMapper.selectCommentsByBoardId(boardId);

        Map<String, Object> result = new HashMap<>();
        result.put("board", board);
        result.put("comments", comments);

        return result;
    }

    @Transactional
    public void updateBoard(BoardEntity board) {
        boardMapper.updateBoard(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        commentMapper.deleteCommentsByBoardId(boardId);
        boardMapper.deleteBoard(boardId);
    }

    @Transactional
    public void createComment(CommentEntity comment) {
        commentMapper.insertComment(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentMapper.deleteComment(commentId);
    }
}