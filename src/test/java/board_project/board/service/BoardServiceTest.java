package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.dto.*;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ResourceNotFoundException;
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
class BoardServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired BoardService boardService;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Test
    void 게시글_작성_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");

        //when
        Long boardId = getBoardId("게시글A", "내용A", userId);
        em.flush(); //쿼리 날리기
        em.clear(); // 1차 캐시 비우기

        //then
        BoardResponseDto findBoard = boardService.findBoard(boardId);
        assertThat(findBoard.getWriterName()).isEqualTo("회원A");
        assertThat(findBoard.getTitle()).isEqualTo("게시글A");
    }


    @Test
    void 게시글_수정_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        em.flush();
        em.clear();

        //when
        BoardUpdateRequestDto boardUpdateDto = new BoardUpdateRequestDto("게시글B", "내용B");
        boardService.updateBoard(boardId, boardUpdateDto, "spring@gmail.com");
        em.flush();
        em.clear();

        //then
        BoardResponseDto findBoard = boardService.findBoard(boardId);
        assertThat(findBoard.getWriterName()).isEqualTo("회원A");
        assertThat(findBoard.getTitle()).isEqualTo("게시글B");
    }

    @Test
    void 권한_없는_게시글_수정_에러_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        em.flush();
        em.clear();

        //when
        BoardUpdateRequestDto boardUpdateDto = new BoardUpdateRequestDto("게시글B", "내용B");

        AccessDeniedException e = assertThrows(AccessDeniedException.class,
                ()-> boardService.updateBoard(boardId, boardUpdateDto, "java@gmail.com"));

        //then
        assertThat(e.getMessage()).isEqualTo("해당 게시글의 수정 권한이 없습니다.");
    }


    @Test
    void 게시글_삭제_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId(userId, boardId);

        //when
        boardService.removeBoard(boardId, "spring@gmail.com");
        em.flush();
        em.clear();

        //then
        ResourceNotFoundException e1 = assertThrows(ResourceNotFoundException.class,
                ()->boardService.findBoard(boardId));
        assertThat(e1.getMessage()).isEqualTo("해당 게시글이 존재하지 않습니다. 게시글 Id : " + boardId);
        ResourceNotFoundException e2 = assertThrows(ResourceNotFoundException.class,
                () -> commentService.findComment(commentId));
        assertThat(e2.getMessage()).isEqualTo("해당 댓글이 존재하지 않습니다. 댓글 Id : " + commentId);
        assertThat(userRepository.findOne(userId).orElseThrow().getBoards().size()).isEqualTo(0);
    }

    @Test
    void 권한_없는_게시글_삭제_에러_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId = getBoardId("게시글A", "내용A", userId);
        Long commentId = getCommentId(userId, boardId);

        //when
        AccessDeniedException e = assertThrows(AccessDeniedException.class,
                ()-> boardService.removeBoard(boardId, "java@gmail.com"));

        //then
        assertThat(e.getMessage()).isEqualTo("해당 게시글의 삭제 권한이 없습니다.");
    }


    @Test
    void 작성자별_게시글_조회_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId1 = getBoardId("게시글A", "내용A", userId);
        Long boardId2 = getBoardId("게시글B", "내용B", userId);
        em.flush();
        em.clear();

        //when
        List<BoardResponseDto> boards = boardService.findBoardsByUser(userId);

        //then
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).extracting("boardId").containsExactlyInAnyOrder(boardId1, boardId2);
    }

    @Test
    void 게시글_전체_조회_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");
        Long boardId1 = getBoardId("게시글A", "내용A", userId);
        Long boardId2 = getBoardId("게시글B", "내용B", userId);
        em.flush();
        em.clear();

        //when
        List<BoardResponseDto> boards = boardService.findBoards();

        //then
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).extracting("boardId").containsExactlyInAnyOrder(boardId1, boardId2);
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

    private Long getCommentId(Long userId, Long boardId) {
        CommentSaveRequestDto commentDto = new CommentSaveRequestDto("댓글A");
        UserResponseDto user = userService.findUser(userId);
        Long commentId = commentService.write(user.getEmail(), boardId, commentDto);
        return commentId;
    }

}