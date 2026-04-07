package board_project.board.dto;

import board_project.board.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveRequestDto {
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String content;
}
