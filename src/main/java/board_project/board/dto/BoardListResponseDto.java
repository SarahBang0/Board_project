package board_project.board.dto;

import board_project.board.domain.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardListResponseDto {
    private Long boardId;
    private String title;
    private String writerName;
    private String createdDate;

    public BoardListResponseDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.writerName = board.getUser().getName();
        this.createdDate = board.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }
}
