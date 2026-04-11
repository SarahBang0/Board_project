package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.Comment;
import board_project.board.domain.User;
import board_project.board.dto.CommentResponseDto;
import board_project.board.dto.CommentSaveRequestDto;
import board_project.board.dto.CommentUpdateRequestDto;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ErrorCode;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.CommentRepository;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 생성
    @Transactional
    public Long write(String email, Long boardId, CommentSaveRequestDto dto) {

        User user = userRepository.findByEmail(email).orElseThrow(
                ()->{
                    log.warn("[회원 조회 실패] 존재하지 않는 회원 - 회원 Email : {}", email);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,
                            "해당 회원이 존재하지 않습니다. 회원 email: " + email);
                });

        Board board = boardRepository.findOne(boardId).orElseThrow(
                ()-> {
                    log.warn("[게시글 조회 실패] 존재하지 않는 게시글 - 게시글 ID : {}", boardId);
                    return new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,
                            "해당 게시글이 존재하지 않습니다. 게시글 ID : " + boardId);
                });

        Comment comment = Comment.createComment(dto.getContent(), user, board, LocalDateTime.now());
        commentRepository.save(comment);
        return comment.getId();
    }


    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto dto, String loginEmail) {
        Comment comment = findCommentOrThrow(commentId);
        isOwnerForUpdate(loginEmail, comment);
        comment.updateComment(dto.getContent(), LocalDateTime.now());
    }


    // 댓글 삭제
    @Transactional
    public void removeComment(Long commentId, String loginEmail) {
        Comment comment = findCommentOrThrow(commentId);
        isOwnerForDelete(loginEmail, comment);
        comment.remove();
        commentRepository.remove(comment);
    }

    // 댓글 조회
    public CommentResponseDto findComment(Long commentId) {
        Comment comment = findCommentOrThrow(commentId);
        return new CommentResponseDto(comment);
    }


    // 게시글 별 댓글 조회
    public List<CommentResponseDto> findCommentsByBoard(Long boardId) {
        boardRepository.findOne(boardId).orElseThrow(
                ()-> {
                    log.warn("[게시글 조회 실패] 존재하지 않는 게시글 - 게시글 ID : {}", boardId);
                    return new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,
                            "해당 게시글이 존재하지 않습니다. 게시글 ID : "+boardId);
                });
        return commentRepository.findByBoard(boardId).stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    // 작성자 별 댓글 조회
    public List<CommentResponseDto> findCommentsByUser(Long userId) {
        userRepository.findOne(userId).orElseThrow(
                ()-> {
                    log.warn("[회원 조회 실패] 존재하지 않는 회원 - 회원 ID : {}", userId);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,
                            "해당 회원을 찾을 수 없습니다. 회원 ID : "+userId);
                });
        return commentRepository.findByUser(userId).stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    // 댓글 존재 여부
    private Comment findCommentOrThrow(Long commentId) {
        Comment comment = commentRepository.findOne(commentId).orElseThrow(
                ()-> {
                    log.warn("[댓글 조회 실패] 존재하지 않는 댓글 - 댓글 ID : {}", commentId);
                    return new ResourceNotFoundException(ErrorCode.COMMENT_NOT_FOUND,
                            "해당 댓글이 존재하지 않습니다. 댓글 ID : " + commentId);
                });
        return comment;
    }

    // 본인 확인 로직
    private static void isOwnerForDelete(String loginEmail, Comment comment) {
        if(comment.isNotOwner(loginEmail)) {
            log.warn("[삭제 권한 없음] - 사용자 : {}, 작성자 : {}, 댓글 ID : {}",
                    loginEmail, comment.getUser().getEmail(), comment.getId());
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "해당 댓글의 삭제 권한이 없습니다.");
        }
    }

    private static void isOwnerForUpdate(String loginEmail, Comment comment) {
        if(comment.isNotOwner(loginEmail)) {
            log.warn("[수정 권한 없음] - 사용자 : {}, 작성자 : {}, 댓글 ID : {}",
                    loginEmail, comment.getUser().getEmail(), comment.getId());
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "해당 댓글의 수정 권한이 없습니다.");
        }
    }


}
