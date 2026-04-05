package board_project.board.controller;

import board_project.board.dto.BoardResponseDto;
import board_project.board.dto.CommentResponseDto;
import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.dto.CommentUpdateRequestDto;
import board_project.board.service.BoardService;
import board_project.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/users/{userId}/boards/{boardId}/comments")
    public String addComment(@PathVariable Long userId,
                             @PathVariable Long boardId,
                             CommentSaveRequestDto dto) {
        Long commentId = commentService.write(userId, boardId, dto);
        return "redirect:/boards/" + boardId;
    }

    // 댓글 수정 폼 이동
    @GetMapping("/boards/{boardId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable Long boardId,
                                @PathVariable Long commentId, Model model) {
        CommentResponseDto comment = commentService.findComment(commentId);
        model.addAttribute("comment", comment);
        model.addAttribute("boardId", boardId);
        return "comments/edit";
    }

    // 댓글 수정
    @PostMapping("/boards/{boardId}/comments/{commentId}/edit")
    public String update(@PathVariable Long boardId,
                         @PathVariable Long commentId,
                         CommentUpdateRequestDto dto) {
        commentService.updateComment(commentId, dto);
        return "redirect:/boards/" + boardId;

    }

    // 댓글 삭제
    @PostMapping("/boards/{boardId}/comments/{commentId}/delete")
    public String remove(@PathVariable Long boardId,
                         @PathVariable Long commentId) {
        commentService.removeComment(commentId);
        return "redirect:/boards/"+ boardId;
    }
}
