package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.Comment;
import board_project.board.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired UserService userService;
    @Autowired BoardService boardService;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;

    @Test
    void 댓글_작성_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId = boardService.write(userId, "게시글A", "내용A");

        //when
        Long commentId = commentService.write("댓글A", userId, boardId);
        em.flush();
        em.clear();

        //then
        Comment findComment = commentService.findComment(commentId);
        assertThat(findComment.getContent()).isEqualTo("댓글A");
    }

    @Test
    void 댓글_수정_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId = boardService.write(userId, "게시글A", "내용A");
        Long commentId = commentService.write("댓글A", userId, boardId);

        //when
        commentService.updateComment(commentId, "수정된 댓글");
        em.flush();
        em.clear();

        //then
        Comment findComment = commentService.findComment(commentId);
        assertThat(findComment.getContent()).isEqualTo("수정된 댓글");
        assertThat(findComment.getUser().getName()).isEqualTo("회원A");
        assertThat(findComment.getBoard().getTitle()).isEqualTo("게시글A");
    }

    @Test
    void 댓글_삭제_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId = boardService.write(userId, "게시글A", "내용A");
        Long commentId = commentService.write("댓글A", userId, boardId);

        //when
        commentService.removeComment(commentId);
        em.flush();
//        em.clear();

        //then
        Comment findComment = commentService.findComment(commentId);
        User findUser = userService.findUser(userId);
        Board findBoard = boardService.findBoard(boardId);

        assertThat(findComment).isNull();
        assertThat(findUser.getComments().size()).isEqualTo(0);
        assertThat(findBoard.getComments().size()).isEqualTo(0);
    }

    @Test
    void 게시물별_댓글_조회_테스트() {
        //given
        Long userId = userService.join("회원A", "spring@gmail.com");
        Long boardId1 = boardService.write(userId, "게시글A", "내용A");
        Long boardId2 = boardService.write(userId, "게시글B", "내용B");
        Long commentId1 = commentService.write("댓글A", userId, boardId1);
        Long commentId2 = commentService.write("댓글B", userId, boardId2);
        em.flush();
        em.clear();

        //when
        List<Comment> comments1 = commentService.findCommentsByBoard(boardId1);
        List<Comment> comments2 = commentService.findCommentsByBoard(boardId2);

        //then
        assertThat(comments1.size()).isEqualTo(1);
        assertThat(comments1.get(0).getBoard().getTitle()).isEqualTo("게시글A");
        assertThat(comments2.get(0).getBoard().getTitle()).isEqualTo("게시글B");
    }

    @Test
    void 작성자별_댓글_조회_테스트() {
        //given
        Long userId1 = userService.join("회원A", "spring@gmail.com");
        Long userId2 = userService.join("회원B", "springnew@gmail.com");
        Long boardId = boardService.write(userId1, "게시글A", "내용A");
        Long commentId1 = commentService.write("댓글A", userId1, boardId);
        Long commentId2 = commentService.write("댓글B", userId2, boardId);
        em.flush();
        em.clear();

        //when
        List<Comment> comments1 = commentService.findCommentsByUser(userId1);
        List<Comment> comments2 = commentService.findCommentsByUser(userId2);

        //then
        assertThat(comments1.size()).isEqualTo(1);
        assertThat(comments1.get(0).getUser().getName()).isEqualTo("회원A");
        assertThat(comments2.get(0).getUser().getName()).isEqualTo("회원B");
    }

}