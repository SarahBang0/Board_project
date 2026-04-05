package board_project.board.dto;

import board_project.board.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserJoinRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;

    public User toEntity() {
        return User.createUser(name, email, LocalDateTime.now());
    }
}
