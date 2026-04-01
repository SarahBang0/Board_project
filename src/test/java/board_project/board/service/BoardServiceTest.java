package board_project.board.service;

import board_project.board.domain.Board;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired UserService userService;
    @Autowired BoardService boardService;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;

    @Test
    void 게시글_작성_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");

        //when
        Long boardId = boardService.write(userId, "게시글A", "내용A");
        em.flush(); //쿼리 날리기
        em.clear(); // 1차 캐시 비우기

        //then
        Board findBoard = boardService.findBoard(boardId);
        assertThat(findBoard.getUser().getName()).isEqualTo("회원A");
        assertThat(findBoard.getTitle()).isEqualTo("게시글A");
    }

    @Test
    void 게시글_수정_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId = boardService.write(userId, "게시글A", "내용A");
        em.flush();
        em.clear();

        //when
        boardService.updateBoard(boardId, "게시글B", "내용B");
        em.flush();
        em.clear();

        //then
        Board findBoard = boardService.findBoard(boardId);
        assertThat(findBoard.getUser().getName()).isEqualTo("회원A");
        assertThat(findBoard.getTitle()).isEqualTo("게시글B");
    }

    @Test
    void 게시글_삭제_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId = boardService.write(userId, "게시글A", "내용A");
        Long commentId = commentService.write("댓글A", userId, boardId);

        //when
        boardService.removeBoard(boardId);
        em.flush();
        em.clear();

        //then
        Board findBoard = boardService.findBoard(boardId);
        assertThat(findBoard).isNull();
        assertThat(commentService.findComment(commentId)).isNull();
        assertThat(userService.findUser(userId).getBoards().size()).isEqualTo(0);
    }

    @Test
    void 작성자별_게시글_조회_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId1 = boardService.write(userId, "게시글A", "내용A");
        Long boardId2 = boardService.write(userId, "게시글B", "내용B");
        em.flush();
        em.clear();

        //when
        List<Board> boards = boardService.findBoardsByUser(userId);
        Board board1 = boardService.findBoard(boardId1);
        Board board2 = boardService.findBoard(boardId2);

        //then
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).contains(board1, board2);
        assertThat(boards.get(0).getUser()).isEqualTo(userService.findUser(userId));
    }

    @Test
    void 게시글_전체_조회_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId1 = boardService.write(userId, "게시글A", "내용A");
        Long boardId2 = boardService.write(userId, "게시글B", "내용B");
        em.flush();
        em.clear();

        //when
        List<Board> boards = boardService.findBoards();
        Board board1 = boardService.findBoard(boardId1);
        Board board2 = boardService.findBoard(boardId2);

        //then
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).contains(board1, board2);
    }

}