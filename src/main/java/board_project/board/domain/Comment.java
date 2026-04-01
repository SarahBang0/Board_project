package board_project.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "COMMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //==생성 메서드==//
    public static Comment createComment(String content, User user, Board board) {
        Comment comment = new Comment();
        comment.content = content;

        comment.confirmUser(user);
        comment.confirmBoard(board);

        return comment;
    }

    //==수정 메서드==//
    public void updateComment(String content) {
        this.content = content;
    }

    //==연관관계 메서드==//
    private void confirmUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    private void confirmBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public void remove() {
        if(this.user!=null) {
            this.user.getComments().remove(this);
        }
        if(this.board!=null) {
            this.board.getComments().remove(this);
        }
    }
}
