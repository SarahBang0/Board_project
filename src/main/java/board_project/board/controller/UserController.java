package board_project.board.controller;

import board_project.board.dto.UserJoinRequestDto;
import board_project.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/join")
    public String joinForm() {
        return "users/join";
    }

    @PostMapping("/users")
    public String join(UserJoinRequestDto dto) {
        userService.join(dto);
        return "redirect:/";
    }
    @GetMapping("/")
    public String back() {
        return "index";
    }
}
