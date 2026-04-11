package board_project.board.apiController;

import board_project.board.dto.CommentResponseDto;
import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.dto.CommentUpdateRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ErrorCode;
import board_project.board.service.CommentService;
import board_project.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final UserService userService;

    // 댓글 작성
    @PostMapping("/api/boards/{boardId}/comments")
    public Long write(@AuthenticationPrincipal UserDetails userDetails,
                      @PathVariable Long boardId,
                      @RequestBody @Valid CommentSaveRequestDto dto) {
        if(userDetails == null) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "로그인이 필요합니다.");
        }
        return commentService.write(userDetails.getUsername(), boardId, dto);
    }

    // 댓글 수정
    @PatchMapping("/api/comments/{commentId}")
    public CommentResponseDto update(@PathVariable Long commentId,
                                     @RequestBody @Valid CommentUpdateRequestDto dto,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        commentService.updateComment(commentId, dto, userDetails.getUsername());
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
    public void remove(@PathVariable Long commentId,
                       @AuthenticationPrincipal UserDetails userDetails) {
        commentService.removeComment(commentId, userDetails.getUsername());
    }
}
