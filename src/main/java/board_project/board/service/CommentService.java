package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.Comment;
import board_project.board.domain.User;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.CommentRepository;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 생성
    @Transactional
    public Long write(String content, Long userId, Long boardId) {
        User user = userRepository.findOne(userId);
        Board board = boardRepository.findOne(boardId);
        Comment comment = Comment.createComment(content, user, board);
        commentRepository.save(comment);
        return comment.getId();
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findOne(commentId);
        comment.updateComment(content);
    }

    // 댓글 삭제
    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findOne(commentId);
        comment.remove();
        commentRepository.remove(comment);
    }

    // 댓글 조회
    public Comment findComment(Long commentId)  {
        return commentRepository.findOne(commentId);
    }

    // 게시글 별 댓글 조회
    public List<Comment> findCommentsByBoard(Long boardId) {
        return commentRepository.findByBoard(boardId);
    }

    // 작성자 별 댓글 조회
    public List<Comment> findCommentsByUser(Long userId) {
        return commentRepository.findByUser(userId);
    }

}
