package board_project.board.controller;

import board_project.board.dto.BoardResponseDto;
import board_project.board.dto.BoardSaveRequestDto;
import board_project.board.dto.BoardUpdateRequestDto;
import board_project.board.service.BoardService;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 목록 보기
    @GetMapping("/boards")
    public String getList(Model model) {
        List<BoardResponseDto> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "boards/list";
    }

    // 글 작성 폼 이동
    @GetMapping("/boards/new")
    public String createForm() {
        return "boards/create";
    }

    // 글 작성 폼
    @PostMapping("/boards")
    public String create(@AuthenticationPrincipal UserDetails userDetails, @Valid BoardSaveRequestDto dto) {
        String email = userDetails.getUsername();
        Long boardId = boardService.write(email, dto);
        return "redirect:/boards/" + boardId;
    }

    // 글 상세 보기
    @GetMapping("/boards/{boardId}")
    public String getDetail(@PathVariable Long boardId, Model model) {
        BoardResponseDto board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.findCommentsByBoard(boardId));
        model.addAttribute("userId", 1L);
        System.out.println("board.getWriterEmail() = " + board.getWriterEmail());
        return "boards/detail";
    }

    // 글 수정 폼
    @GetMapping("/boards/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        BoardResponseDto board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        return "boards/edit";
    }

    // 글 수정
    @PostMapping("/boards/{boardId}")
    public String getEdit(@PathVariable Long boardId, @Valid BoardUpdateRequestDto dto,
                          @AuthenticationPrincipal UserDetails userDetails) {
        boardService.updateBoard(boardId, dto, userDetails.getUsername());
        return "redirect:/boards/" + boardId;
    }

    // 글 삭제
    @PostMapping("/boards/{boardId}/delete")
    public String delete(@PathVariable Long boardId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        boardService.removeBoard(boardId, userDetails.getUsername());
        return "redirect:/boards";
    }
}
