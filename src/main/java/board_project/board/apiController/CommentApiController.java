package board_project.board.apiController;

import board_project.board.dto.CommentResponseDto;
import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.dto.CommentUpdateRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.service.CommentService;
import board_project.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final UserService userService;

    // 댓글 작성
    @PostMapping("/api/users/{userId}/boards/{boardId}/comments")
    public Long write(@PathVariable Long userId, @PathVariable Long boardId,
                      @RequestBody @Valid CommentSaveRequestDto dto) {
        UserResponseDto user = userService.findUser(userId);
        return commentService.write(user.getEmail(), boardId, dto);
    }

    // 댓글 수정
    @PatchMapping("/api/comments/{commentId}")
    public CommentResponseDto update(@PathVariable Long commentId,
                                     @RequestBody @Valid CommentUpdateRequestDto dto) {
        commentService.updateComment(commentId, dto);
        return commentService.findComment(commentId);
    }

    // 단일 댓글 조회
    @GetMapping("/api/comments/{commentId}")
    public CommentResponseDto findComment(@PathVariable Long commentId) {
        return commentService.findComment(commentId);
    }

    // 게시물 별 댓글 조회
    @GetMapping("/api/boards/{boardId}/comments")
    public List<CommentResponseDto> findCommentsByBoard(@PathVariable Long boardId) {
        return commentService.findCommentsByBoard(boardId);
    }

    // 작성자 별 댓글 조회
    @GetMapping("/api/users/{userId}/comments")
    public List<CommentResponseDto> findCommentsByUser(@PathVariable Long userId) {
        return commentService.findCommentsByUser(userId);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public void remove(@PathVariable Long commentId) {
        commentService.removeComment(commentId);
    }
}
