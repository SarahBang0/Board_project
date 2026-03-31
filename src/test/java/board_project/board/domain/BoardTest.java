package board_project.board.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    void 게시글_생성_테스트() {
        //givin
        User user = getUser();

        //when
        Board board = getBoard("첫 게시글", user);

        //then
        assertThat(board.getTitle()).isEqualTo("첫 게시글");
        assertThat(board.getUser().getName()).isEqualTo("스프링");
    }

    @Test
    void 게시글_여러개_생성_테스트() {
        //givin
        User user = getUser();

        //when
        Board board1 = getBoard("게시글A", user);
        Board board2 = getBoard("게시글B", user);

        //then
        assertThat(user.getBoards().size()).isEqualTo(2);
        assertThat(user.getBoards()).contains(board1, board2);
    }

    @Test
    void 게시글_수정_테스트() {
        //given
        User user = getUser();
        Board board = getBoard("첫 게시글", user);

        //when
        board.updateBoard("수정된 게시글", "수정 내용");

        //then
        assertThat(board.getTitle()).isEqualTo("수정된 게시글");
        assertThat(board.getContent()).isEqualTo("수정 내용");
        assertThat(board.getUser()).isEqualTo(user);
    }

    private static User getUser() {
        User user = User.createUser("스프링","spring@gmail.com");
        return user;
    }

    private static Board getBoard(String title, User user) {
        Board board = Board.createBoard(title, "내용 ", user);
        return board;
    }
}
