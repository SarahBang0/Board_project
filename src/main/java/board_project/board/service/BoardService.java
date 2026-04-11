package board_project.board.service;

import board_project.board.domain.Board;
import board_project.board.domain.User;
import board_project.board.dto.BoardDetailResponseDto;
import board_project.board.dto.BoardListResponseDto;
import board_project.board.dto.BoardSaveRequestDto;
import board_project.board.dto.BoardUpdateRequestDto;
import board_project.board.exception.AccessDeniedException;
import board_project.board.exception.ErrorCode;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.repository.BoardRepository;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // 게시글 작성
    @Transactional
    public Long write(String email, BoardSaveRequestDto dto) {

        User user = userRepository.findByEmail(email).orElseThrow(
                ()->{
                    log.warn("[회원 조회 실패] 존재하지 않는 회원 - 회원 Email : {}", email);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,
                            "해당 회원이 존재하지 않습니다. 회원 Email: " + email);
                });

        Board board = Board.createBoard(dto.getTitle(), dto.getContent(), user, LocalDateTime.now());
        boardRepository.save(board);
        return board.getId();
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, BoardUpdateRequestDto dto, String loginEmail) {
        Board board = findBoardOrThrow(boardId);
        isOwnerForUpdate(loginEmail, board);
        board.updateBoard(dto.getTitle(), dto.getContent());
    }


    // 게시글 삭제
    @Transactional
    public void removeBoard(Long boardId, String loginEmail) {
        Board board = findBoardOrThrow(boardId);
        isOwnerForDelete(loginEmail, board);
        board.remove();
        boardRepository.remove(board);
    }



    // 게시글 목록 조회
    public List<BoardListResponseDto> findBoards() {
        return boardRepository.findAll().stream()
                .map(BoardListResponseDto::new)
                .toList();
    }

    // 게시글 단 건 조회
    public BoardDetailResponseDto findBoard(Long boardId) {
        Board board = findBoardOrThrow(boardId);
        return new BoardDetailResponseDto(board);
    }

    // 작성자 별 게시글 조회
    public List<BoardListResponseDto> findBoardsByUser(Long userId) {
        return boardRepository.findByUser(userId).stream()
                .map(BoardListResponseDto::new)
                .toList();
    }

    // 제목으로 게시글 검색하기
    public List<BoardListResponseDto> searchBoards(String keyword) {
        if(keyword == null && keyword.trim().isEmpty()) {
            return boardRepository.findAll().stream().map(BoardListResponseDto::new).toList();
        }
        return boardRepository.findByTitle(keyword).stream().map(BoardListResponseDto::new).toList();
    }


    //게시물 존재 확인
    private Board findBoardOrThrow(Long boardId) {
        Board board = boardRepository.findOne(boardId).orElseThrow(
                ()->{
                    log.warn("[게시글 조회 실패] 존재하지 않는 게시글 - 게시글 ID : {}", boardId);
                    return new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,
                            "해당 게시글이 존재하지 않습니다. 게시글 ID : " + boardId);
                });
        return board;
    }

    // 본인 확인 로직
    private static void isOwnerForDelete(String loginEmail, Board board) {
        if (board.isNotOwner(loginEmail)) {
            log.warn("[삭제 권한 없음] - 사용자 : {}, 작성자 : {}, 게시글 ID : {}",
                    loginEmail, board.getUser().getEmail(), board.getId());
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "해당 게시글의 삭제 권한이 없습니다.");
        }
    }

    private static void isOwnerForUpdate(String loginEmail, Board board) {
        if (board.isNotOwner(loginEmail)) {
            log.warn("[수정 권한 없음] - 사용자 : {}, 작성자 : {}, 게시글 ID : {}",
                    loginEmail, board.getUser().getEmail(), board.getId());
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED, "해당 게시글의 수정 권한이 없습니다.");
        }
    }



}
