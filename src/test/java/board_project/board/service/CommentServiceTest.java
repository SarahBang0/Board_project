package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.Comment;
import board_project.board.domain.User;
import board_project.board.dto.*;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Test
    void 댓글_작성_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);

        //when
        Long commentId = getCommentId("댓글A", userId, boardId);
        em.flush();
        em.clear();

        //then
        CommentResponseDto findComment = commentService.findComment(commentId);
        assertThat(findComment.getContent()).isEqualTo("댓글A");
    }



    @Test
    void 댓글_수정_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId("댓글A", userId, boardId);

        //when
        CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto("수정된 댓글");
        commentService.updateComment(commentId, commentUpdateDto, "spring@gmail.com");
        em.flush();
        em.clear();

        //then
        CommentResponseDto findComment = commentService.findComment(commentId);
        assertThat(findComment.getContent()).isEqualTo("수정된 댓글");
        assertThat(findComment.getWriterName()).isEqualTo("회원A");
        assertThat(findComment.getBoardTitle()).isEqualTo("게시글A");
    }

    @Test
    void 권한_없는_댓글_수정_에러_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId("댓글A", userId, boardId);

        //when
        CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto("수정된 댓글");
        AccessDeniedException e = assertThrows(AccessDeniedException.class,
                ()-> commentService.updateComment(commentId, commentUpdateDto, "java@gmail.com"));

        //then
        assertThat(e.getMessage()).isEqualTo("해당 댓글의 수정 권한이 없습니다.");
    }



    @Test
    void 댓글_삭제_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId("댓글A", userId, boardId);

        //when
        commentService.removeComment(commentId, "spring@gmail.com");
        em.flush();
//        em.clear();

        //then
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                ()->commentService.findComment(commentId));
        User findUser = userRepository.findOne(userId).orElseThrow();
        Board findBoard = boardRepository.findOne(boardId).orElseThrow();

        assertThat(e.getMessage()).isEqualTo("해당 댓글이 존재하지 않습니다. 댓글 Id : "+commentId);
        assertThat(findUser.getComments().size()).isEqualTo(0);
        assertThat(findBoard.getComments().size()).isEqualTo(0);
    }

    @Test
    void 권한_없는_댓글_삭제_에러_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId("댓글A", userId, boardId);

        //when
        AccessDeniedException e = assertThrows(AccessDeniedException.class,
                () -> commentService.removeComment(commentId, "java@gmail.com"));

        //then
        assertThat(e.getMessage()).isEqualTo("해당 댓글의 삭제 권한이 없습니다.");

    }


    @Test
    void 게시물별_댓글_조회_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId1 = getBoardId("게시글A", "내용A", userId);
        Long boardId2 = getBoardId("게시글B", "내용B", userId);
        Long commentId1 = getCommentId("댓글A", userId, boardId1);
        Long commentId2 = getCommentId("댓글B", userId, boardId2);

        em.flush();
        em.clear();

        //when
        List<CommentResponseDto> comments1 = commentService.findCommentsByBoard(boardId1);
        List<CommentResponseDto> comments2 = commentService.findCommentsByBoard(boardId2);

        //then
        assertThat(comments1.size()).isEqualTo(1);
        assertThat(comments1.get(0).getBoardTitle()).isEqualTo("게시글A");
        assertThat(comments2.get(0).getBoardTitle()).isEqualTo("게시글B");
    }

    @Test
    void 작성자별_댓글_조회_테스트() {
        //given
        Long userId1 = getUserId("회원A", "spring@gmail.com", "1234");
        Long userId2 = getUserId("회원B", "jpa@gmail.com", "1234");
        Long boardId1 = getBoardId("게시글A", "내용A", userId1);
        Long boardId2 = getBoardId("게시글B", "내용B", userId2);
        getCommentId("댓글A", userId1, boardId1);
        getCommentId("댓글B", userId2, boardId2);
        em.flush();
        em.clear();

        //when
        List<CommentResponseDto> comments1 = commentService.findCommentsByUser(userId1);
        List<CommentResponseDto> comments2 = commentService.findCommentsByUser(userId2);

        //then
        assertThat(comments1.size()).isEqualTo(1);
        assertThat(comments1.get(0).getWriterName()).isEqualTo("회원A");
        assertThat(comments2.get(0).getWriterName()).isEqualTo("회원B");
    }



    private Long getUserId(String name, String email, String password) {
        UserJoinRequestDto userDto1 = new UserJoinRequestDto(name, email, password);
        Long userId = userService.join(userDto1);
        return userId;
    }

    private Long getBoardId(String title, String content, Long userId) {
        BoardSaveRequestDto boardDto = new BoardSaveRequestDto(title, content);
        UserResponseDto user = userService.findUser(userId);
        Long boardId = boardService.write(user.getEmail(), boardDto);
        return boardId;
    }

    private Long getCommentId(String content, Long userId, Long boardId) {
        CommentSaveRequestDto commentDto = new CommentSaveRequestDto(content);
        UserResponseDto user = userService.findUser(userId);
        Long commentId = commentService.write(user.getEmail(), boardId, commentDto);
        return commentId;
    }

}