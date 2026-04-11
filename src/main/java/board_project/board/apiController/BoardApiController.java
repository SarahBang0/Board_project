package board_project.board.apiController;

import board_project.board.dto.BoardResponseDto;
import board_project.board.dto.BoardSaveRequestDto;
import board_project.board.dto.BoardUpdateRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ErrorCode;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.service.BoardService;
import board_project.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final UserService userService;

    // 게시글 작성
    @PostMapping("/api/boards")
    public Long write(@AuthenticationPrincipal UserDetails userDetails,
                      @RequestBody @Valid BoardSaveRequestDto dto) {

        if(userDetails == null) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "로그인이 필요합니다.");
        }
        String email = userDetails.getUsername();
        return boardService.write(email, dto);
    }

    // 게시글 수정
    @PatchMapping("/api/boards/{boardId}")
    public BoardResponseDto update(@PathVariable Long boardId,
                                   @RequestBody @Valid BoardUpdateRequestDto dto,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        boardService.updateBoard(boardId, dto, userDetails.getUsername());
        return boardService.findBoard(boardId);
    }

    // 전체 게시글 조회
    @GetMapping("/api/boards")
    public List<BoardResponseDto> findBoards() {
        return boardService.findBoards();
    }

    // 단일 게시글 조회
    @GetMapping("/api/boards/{boardId}")
    public BoardResponseDto findBoard(@PathVariable Long boardId) {
        return boardService.findBoard(boardId);
    }

    // 작성자별 게시글 조회
    @GetMapping("/api/users/{userId}/boards")
    public List<BoardResponseDto> findBoardsByUser(@PathVariable Long userId) {
        return boardService.findBoardsByUser(userId);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{boardId}")
    public void remove(@PathVariable Long boardId,
                       @AuthenticationPrincipal UserDetails userDetails) {
        boardService.removeBoard(boardId, userDetails.getUsername());
    }
}
