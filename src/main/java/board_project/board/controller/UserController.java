package board_project.board.controller;

import board_project.board.dto.*;
import board_project.board.service.BoardService;
import board_project.board.service.CommentService;
import board_project.board.service.UserService;
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
public class UserController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    // 회원 가입 폼 이동
    @GetMapping("/users/join")
    public String joinForm() {
        return "users/join";
    }

    // 회원 가입
    @PostMapping("/users/join")
    public String join(@Valid UserJoinRequestDto dto) {
        userService.join(dto);
        return "redirect:/users";
    }

    // 뒤로 가기
    @GetMapping("/")
    public String back() {
        return "index";
    }

    // 회원 목록
    @GetMapping("/users")
    public String userList(Model model) {
        List<UserResponseDto> users = userService.findUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    // 회원 상세 보기
    @GetMapping("/users/{userId}")
    public String userDetail(@PathVariable Long userId, Model model) {
        UserResponseDto user = userService.findUser(userId);
        List<BoardListResponseDto> boardsByUser = boardService.findBoardsByUser(userId);
        List<CommentResponseDto> commentsByUser = commentService.findCommentsByUser(userId);
        model.addAttribute("user", user);
        model.addAttribute("boards", boardsByUser);
        model.addAttribute("comments", commentsByUser);
        return "users/detail";
    }

    // 마이페이지
    @GetMapping("/my-page")
    public String myPage(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserResponseDto user = userService.findByEmail(email);
        Long userId = user.getUserId();
        return "redirect:/users/" + userId;
    }
}
