package board_project.board.dto;

import board_project.board.domain.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String boardTitle;
    private String writerName;
    private String createdDateTime;
    private Long boardId;
    private String writerEmail;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.boardTitle = comment.getBoard().getTitle();
        this.writerName = comment.getUser().getName();
        this.createdDateTime = comment.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.boardId = comment.getBoard().getId();
        this.writerEmail = comment.getUser().getEmail();
    }
}
