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
    public String boardWriteProcess(@RequestParam(required = false) String title,
                                    @RequestParam(required = false) String content,
                                    HttpSession session,
                                    RedirectAttributes ra) {

        // 입력값 검증
        if (title == null || title.trim().isEmpty()) {
            ra.addFlashAttribute("error", "제목을 입력해주세요.");
            return "redirect:/board/boardWrite";
        }

        if (content == null || content.trim().isEmpty()) {
            ra.addFlashAttribute("error", "내용을 입력해주세요.");
            return "redirect:/board/boardWrite";
        }

        // 로그인 체크 - userId 또는 실제 세션 키로 변경
        String writer = (String) session.getAttribute("userId");

        // 로그인 안 되어있으면 임시 작성자 설정 (테스트용)
        if (writer == null || writer.trim().isEmpty()) {
            writer = "익명";  // 실제 운영에서는 로그인 페이지로 리다이렉트
        }

        try {
            BoardEntity board = new BoardEntity();
            board.setTitle(title.trim());
            board.setContent(content.trim());
            board.setWriter(writer);
            boardService.createBoard(board);

            ra.addFlashAttribute("success", "게시글이 등록되었습니다.");
            return "redirect:/board/boardList";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "게시글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/board/boardWrite";
        }
    }

    // 게시글 상세
    @GetMapping("/boardDetail")
    public String boardDetail(@RequestParam Long boardId, Model model) {
        try {
            Map<String, Object> result = boardService.getBoardDetail(boardId);
            model.addAttribute("board", result.get("board"));
            model.addAttribute("comments", result.get("comments"));
            return "board/board-detail";
        } catch (Exception e) {
            model.addAttribute("error", "게시글을 불러올 수 없습니다.");
            return "redirect:/board/boardList";
        }
    }

    // 게시글 수정 페이지
    @GetMapping("/boardEdit")
    public String boardEdit(@RequestParam Long boardId,
                            HttpSession session,
                            Model model,
                            RedirectAttributes ra) {
        try {
            String loginUser = (String) session.getAttribute("userId");
            BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

            // 작성자 확인 (익명 사용자는 수정 가능)
            if (loginUser != null && !board.getWriter().equals(loginUser)) {
                ra.addFlashAttribute("error", "수정 권한이 없습니다.");
                return "redirect:/board/boardDetail?boardId=" + boardId;
            }

            model.addAttribute("board", board);
            return "board/board-edit";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "게시글을 불러올 수 없습니다.");
            return "redirect:/board/boardList";
        }
    }

    // 게시글 수정 처리
    @PostMapping("/boardEdit")
    public String boardEditProcess(@RequestParam Long boardId,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) String content,
                                   HttpSession session,
                                   RedirectAttributes ra) {

        // 입력값 검증
        if (title == null || title.trim().isEmpty()) {
            ra.addFlashAttribute("error", "제목을 입력해주세요.");
            return "redirect:/board/boardEdit?boardId=" + boardId;
        }

        if (content == null || content.trim().isEmpty()) {
            ra.addFlashAttribute("error", "내용을 입력해주세요.");
            return "redirect:/board/boardEdit?boardId=" + boardId;
        }

        try {
            String loginUser = (String) session.getAttribute("userId");
            BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

            // 작성자 확인
            if (loginUser != null && !board.getWriter().equals(loginUser)) {
                ra.addFlashAttribute("error", "수정 권한이 없습니다.");
                return "redirect:/board/boardDetail?boardId=" + boardId;
            }

            board.setTitle(title.trim());
            board.setContent(content.trim());
            boardService.updateBoard(board);

            ra.addFlashAttribute("success", "게시글이 수정되었습니다.");
            return "redirect:/board/boardDetail?boardId=" + boardId;
        } catch (Exception e) {
            ra.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다.");
            return "redirect:/board/boardEdit?boardId=" + boardId;
        }
    }

    // 게시글 삭제
    @PostMapping("/boardDelete")
    public String boardDelete(@RequestParam Long boardId,
                              HttpSession session,
                              RedirectAttributes ra) {
        try {
            String loginUser = (String) session.getAttribute("userId");
            BoardEntity board = (BoardEntity) boardService.getBoardDetail(boardId).get("board");

            // 작성자 확인
            if (loginUser != null && !board.getWriter().equals(loginUser)) {
                ra.addFlashAttribute("error", "삭제 권한이 없습니다.");
                return "redirect:/board/boardDetail?boardId=" + boardId;
            }

            boardService.deleteBoard(boardId);
            ra.addFlashAttribute("success", "게시글이 삭제되었습니다.");
            return "redirect:/board/boardList";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다.");
            return "redirect:/board/boardList";
        }
    }

    // 댓글 작성
    @PostMapping("/commentWrite")
    @ResponseBody
    public Map<String, Object> commentWrite(@RequestParam Long boardId,
                                            @RequestParam(required = false) String content,
                                            HttpSession session) {
        try {
            if (content == null || content.trim().isEmpty()) {
                return Map.of("success", false, "message", "댓글 내용을 입력해주세요.");
            }

            String writer = (String) session.getAttribute("userId");
            if (writer == null || writer.trim().isEmpty()) {
                writer = "익명";
            }

            CommentEntity comment = new CommentEntity();
            comment.setBoardId(boardId);
            comment.setContent(content.trim());
            comment.setWriter(writer);
            boardService.createComment(comment);

            return Map.of("success", true, "message", "댓글이 등록되었습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "댓글 등록 중 오류가 발생했습니다.");
        }
    }

    // 댓글 삭제
    @PostMapping("/commentDelete")
    @ResponseBody
    public Map<String, Object> commentDelete(@RequestParam Long commentId) {
        try {
            boardService.deleteComment(commentId);
            return Map.of("success", true, "message", "댓글이 삭제되었습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "댓글 삭제 중 오류가 발생했습니다.");
        }
    }
}