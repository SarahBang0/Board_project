package board_project.board.dto;

import board_project.board.domain.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String writerName;
    private String createdDate;
    private String createdDateTime;
    private Long userId;
    private String writerEmail;

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerName = board.getUser().getName();
        this.createdDate = board.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.createdDateTime = board.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.userId = board.getUser().getId();
        this.writerEmail = board.getUser().getEmail();
    }
}
