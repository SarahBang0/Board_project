package board_project.board.controller;

import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/boards/{boardId}/comments")
    public String addComment(@PathVariable Long userId,
                             @PathVariable Long boardId,
                             CommentSaveRequestDto dto) {
        Long commentId = commentService.write(userId, boardId, dto);
        return "redirect:/boards/" + boardId;
    }
}
