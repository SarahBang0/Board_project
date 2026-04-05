package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.User;
import board_project.board.dto.BoardResponseDto;
import board_project.board.dto.BoardSaveRequestDto;
import board_project.board.dto.BoardUpdateRequestDto;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    @Transactional
    public Long write(Long userId, BoardSaveRequestDto dto) {
        User user = userRepository.findOne(userId).orElseThrow(
                ()->new ResourceNotFoundException("해당 회원이 존재하지 않습니다. 회원 Id: " + userId));
        Board board = Board.createBoard(dto.getTitle(), dto.getContent(), user, LocalDateTime.now());
        boardRepository.save(board);
        return board.getId();
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, BoardUpdateRequestDto dto) {
        Board board = findBoardOrThrow(boardId);
        board.updateBoard(dto.getTitle(), dto.getContent());
    }


    @Transactional
    public void removeBoard(Long boardId) {
        Board board = findBoardOrThrow(boardId);
        board.remove();
        boardRepository.remove(board);
    }

    // 게시글 목록 조회
    public List<BoardResponseDto> findBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    // 게시글 단 건 조회
    public BoardResponseDto findBoard(Long boardId) {
        Board board = findBoardOrThrow(boardId);
        return new BoardResponseDto(board);
    }

    // 작성자 별 게시글 조회
    public List<BoardResponseDto> findBoardsByUser(Long userId) {
        return boardRepository.findByUser(userId).stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    private Board findBoardOrThrow(Long boardId) {
        Board board = boardRepository.findOne(boardId).orElseThrow(
                ()->new ResourceNotFoundException("해당 게시글이 존재하지 않습니다. 게시글 Id : " + boardId));
        return board;
    }



}
