package board_project.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // Common 공통 에러
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다."),

    // User (회원 관련)
    USER_NOT_FOUND(404, "U001", "해당 회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409, "U002", "이미 사용 중인 이메일입니다."),

    // Board (게시글 관련)
    BOARD_NOT_FOUND(404, "B001", "해당 게시글이 존재하지 않습니다."),
    NOT_BOARD_AUTHOR(403, "B002", "게시글 수정 권한이 없습니다."),


    // Comment(댓글 관련)
    COMMENT_NOT_FOUND(404, "CM001", "해당 댓글이 존재하지 않습니다");


    private final int status;
    private final String code;
    private final String message;


}
