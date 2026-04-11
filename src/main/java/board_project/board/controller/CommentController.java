package board_project.board.controller;

import board_project.board.dto.CommentResponseDto;
import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.dto.CommentUpdateRequestDto;
import board_project.board.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PostMapping("/boards/{boardId}/comments")
    public String addComment(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long boardId,
                             @Valid CommentSaveRequestDto dto) {
        String email = userDetails.getUsername();
        Long commentId = commentService.write(email, boardId, dto);
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
                         CommentUpdateRequestDto dto,
                         @AuthenticationPrincipal UserDetails userDetails) {
        commentService.updateComment(commentId, dto, userDetails.getUsername());
        return "redirect:/boards/" + boardId;

    }

    // 댓글 삭제
    @PostMapping("/boards/{boardId}/comments/{commentId}/delete")
    public String remove(@PathVariable Long boardId,
                         @PathVariable Long commentId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        commentService.removeComment(commentId, userDetails.getUsername());
        return "redirect:/boards/"+ boardId;
    }
}
