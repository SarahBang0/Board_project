package board_project.board.dto;

import board_project.board.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String joinedDate;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.joinedDate = user.getJoinedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
