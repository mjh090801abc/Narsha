package com.spring.dishcovery.controller;

import com.spring.dishcovery.entity.BoardEntity;
import com.spring.dishcovery.entity.CommentEntity;
import com.spring.dishcovery.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록
    @GetMapping("/boardList")
    public String boardList(Model model) {
        model.addAttribute("boardList", boardService.getBoardList());
        return "board/board";
    }

    // 게시글 작성 페이지
    @GetMapping("/boardWrite")
    public String boardWrite() {
        return "board/board-write";
    }

    // 게시글 작성 처리
    @PostMapping("/boardWrite")
    public String boardWriteProcess(@RequestParam String title,
                                    @RequestParam String content,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        String writer = (String) session.getAttribute("userId");
        if (writer == null) {
            ra.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        BoardEntity board = new BoardEntity();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        boardService.createBoard(board);

        ra.addFlashAttribute("success", "게시글이 등록되었습니다.");
        return "redirect:/board/boardList";
    }

    // 게시글 상세
    @GetMapping("/boardDetail")
    public String boardDetail(@RequestParam Long boardId, Model model) {
        Map<String, Object> result = boardService.getBoardDetail(boardId);
        model.addAttribute("board", result.get("board"));
        model.addAttribute("comments", result.get("comments"));
        return "board/board-detail";
    }

    // 게시글 수정 페이지
    @GetMapping("/boardEdit")
    public String boardEdit(@RequestParam Long boardId,
                            HttpSession session,
                            Model model,
                            RedirectAttributes ra) {
        String loginUser = (String) session.getAttribute("userId");
        BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

        if (!board.getWriter().equals(loginUser)) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/board/boardDetail?boardId=" + boardId;
        }

        model.addAttribute("board", board);
        return "board/board-edit";
    }

    // 게시글 수정 처리
    @PostMapping("/boardEdit")
    public String boardEditProcess(@RequestParam Long boardId,
                                   @RequestParam String title,
                                   @RequestParam String content,
                                   HttpSession session,
                                   RedirectAttributes ra) {
        String loginUser = (String) session.getAttribute("userId");
        BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

        if (!board.getWriter().equals(loginUser)) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/board/boardDetail?boardId=" + boardId;
        }

        board.setTitle(title);
        board.setContent(content);
        boardService.updateBoard(board);

        ra.addFlashAttribute("success", "게시글이 수정되었습니다.");
        return "redirect:/board/boardDetail?boardId=" + boardId;
    }

    // 게시글 삭제
    @PostMapping("/boardDelete")
    public String boardDelete(@RequestParam Long boardId,
                              HttpSession session,
                              RedirectAttributes ra) {
        String loginUser = (String) session.getAttribute("userId");
        BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

        if (!board.getWriter().equals(loginUser)) {
            ra.addFlashAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/board/boardDetail?boardId=" + boardId;
        }

        boardService.deleteBoard(boardId);
        ra.addFlashAttribute("success", "게시글이 삭제되었습니다.");
        return "redirect:/board/boardList";
    }

    // 댓글 작성
    @PostMapping("/commentWrite")
    @ResponseBody
    public Map<String, Object> commentWrite(@RequestParam Long boardId,
                                            @RequestParam String content,
                                            HttpSession session) {
        String writer = (String) session.getAttribute("userId");

        CommentEntity comment = new CommentEntity();
        comment.setBoardId(boardId);
        comment.setContent(content);
        comment.setWriter(writer);
        boardService.createComment(comment);

        return Map.of("success", true, "message", "댓글이 등록되었습니다.");
    }

    // 댓글 삭제
    @PostMapping("/commentDelete")
    @ResponseBody
    public Map<String, Object> commentDelete(@RequestParam Long commentId) {
        boardService.deleteComment(commentId);
        return Map.of("success", true, "message", "댓글이 삭제되었습니다.");
    }
}