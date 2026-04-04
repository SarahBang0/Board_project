package board_project.board.dto;

import board_project.board.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserJoinRequestDto {
    private String name;
    private String email;

    public User toEntity() {
        return User.createUser(name, email);
    }
}
