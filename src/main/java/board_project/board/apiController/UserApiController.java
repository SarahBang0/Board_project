package board_project.board.apiController;

import board_project.board.dto.UserJoinRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    // 회원 가입
    @PostMapping
    public Long join(@RequestBody @Valid UserJoinRequestDto dto) {
        return userService.join(dto);
    }

    // 전체 회원 조회
    @GetMapping
    public List<UserResponseDto> users() {
        return userService.findUsers();
    }

    // 단일 회원 조회
    @GetMapping("/{userId}")
    public UserResponseDto user(@PathVariable Long userId) {
        return userService.findUser(userId);
    }
}
