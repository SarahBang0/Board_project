package board_project.board.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CommentTest {

    @Test
    void 댓글_생성_테스트() {
        //given
        User user = getUser();
        Board board = getBoard(user);

        //when
        Comment comment = getComment("댓글", user, board);

        //then
        assertThat(user.getComments().size()).isEqualTo(1);
        assertThat(board.getComments().size()).isEqualTo(1);
        assertThat(comment.getContent()).isEqualTo("댓글");
    }

    @Test
    void 댓글_여러개_생성_테스트() {
        //given
        User user = getUser();
        Board board = getBoard(user);

        //when
        Comment comment1 = getComment("댓글A", user, board);
        Comment comment2 = getComment("댓글B", user, board);

        //then
        assertThat(board.getComments().size()).isEqualTo(2);
        assertThat(user.getComments().size()).isEqualTo(2);
        assertThat(board.getComments()).contains(comment1, comment2);
    }


    @Test
    void 댓글_수정_테스트() {
        //given
        User user = getUser();
        Board board = getBoard(user);
        Comment comment = getComment("댓글", user, board);

        //when
        comment.updateComment("수정된 댓글", LocalDateTime.now());

        //then
        assertThat(comment.getContent()).isEqualTo("수정된 댓글");
        assertThat(comment.getUser()).isNotNull();
        assertThat(comment.getBoard()).isNotNull();
    }


    private static User getUser() {
        User user = User.createUser("스프링","spring@gmail.com", "1234", LocalDateTime.now());
        return user;
    }

    private static Board getBoard(User user) {
        Board board = Board.createBoard("첫 게시글", "내용 ", user, LocalDateTime.now());
        return board;
    }

    private static Comment getComment(String content,User user, Board board) {
        Comment comment = Comment.createComment(content, user, board, LocalDateTime.now());
        return comment;
    }



}

