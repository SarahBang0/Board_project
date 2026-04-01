package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.User;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    @Transactional
    public Long write(Long userId, String title, String content) {
        User user = userRepository.findOne(userId);
        Board board = Board.createBoard(title, content, user);
        boardRepository.save(board);
        return board.getId();
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, String title, String content) {
        Board board = boardRepository.findOne(boardId);
        board.updateBoard(title, content);
    }

    @Transactional
    public void removeBoard(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.remove();
        boardRepository.remove(board);
    }

    // 게시글 목록 조회
    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    // 게시글 단 건 조회
    public Board findBoard(Long boardId) {
        return boardRepository.findOne(boardId);
    }

    // 작성자 별 게시글 조회
    public List<Board> findBoardsByUser(Long userId) {
        return boardRepository.findByUser(userId);
    }
}
