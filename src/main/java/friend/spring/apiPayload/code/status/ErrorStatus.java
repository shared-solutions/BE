package friend.spring.apiPayload.code.status;

import friend.spring.apiPayload.code.BaseErrorCode;
import friend.spring.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON4000","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON4001","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4003", "금지된 요청입니다."),

    // 멤버 관련 응답
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "사용자를 찾을 수 없습니다."),

    // 글 관련 응답
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4001", "글을 찾을 수 없습니다."),
    NOT_CORRECT_USER(HttpStatus.BAD_REQUEST, "POST4002", "올바른 사용자(글 작성자)가 아닙니다."),

    // 댓글 관련 응답
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4001", "댓글을 찾을 수 없습니다."),
    COMMENT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4002", "댓글에 대한 좋아요 데이터를 찾을 수 없습니다."),
    COMMENT_CHOICE_OVER_ONE(HttpStatus.BAD_REQUEST, "COMMENT4003", "댓글 채택은 1개 댓글에 대해서만 가능합니다."),
    COMMENT_SELECT_MYSELF(HttpStatus.BAD_REQUEST, "COMMENT4004", "자기 자신은 채택할 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}

