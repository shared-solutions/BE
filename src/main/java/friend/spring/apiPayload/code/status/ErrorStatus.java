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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "회원정보가 존재하지 않습니다."),
    USERS_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"USER4010","가입 가능한 이메일입니다."),
    USER_EXISTS_EMAIL(HttpStatus.NOT_ACCEPTABLE,"USER4002","이미 존재하는 메일 주소입니다"),
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "USER4003", "이메일을 발송하지 못했습니다."),
    ERR_MAKE_CODE(HttpStatus.BAD_REQUEST, "USER4004", "인증 코드 생성에 오류가 있습니다."),
    INCORRECT_CODE(HttpStatus.UNAUTHORIZED, "USER4005", "인증 코드가 일치하지 않습니다."),
    EMPTY_JWT(HttpStatus.BAD_REQUEST, "USER4006", "JWT를 입력해주세요."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "USER4007", "유효하지 않은 JWT입니다."),
    PASSWORD_INCORRECT(HttpStatus.NOT_FOUND, "USER4008", "비밀번호가 틀렸습니다."),
    RTK_INCORREXT(HttpStatus.UNAUTHORIZED,"USER4100","RefreshToken값을 확인해주세요."),
    // 글 관련 응답
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4001", "글을 찾을 수 없습니다."),
    POST_NOT_CORRECT_USER(HttpStatus.BAD_REQUEST, "POST4002", "올바른 사용자(글 작성자)가 아닙니다."),
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4003", "글에 대한 좋아요 데이터를 찾을 수 없습니다."),

    // 댓글 관련 응답
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4001", "댓글을 찾을 수 없습니다."),
    COMMENT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4002", "댓글에 대한 좋아요 데이터를 찾을 수 없습니다."),
    COMMENT_CHOICE_OVER_ONE(HttpStatus.BAD_REQUEST, "COMMENT4003", "댓글 채택은 1개 댓글에 대해서만 가능합니다."),
    COMMENT_SELECT_MYSELF(HttpStatus.BAD_REQUEST, "COMMENT4004", "자기 자신은 채택할 수 없습니다."),
    COMMENT_NOT_CORRECT_USER(HttpStatus.BAD_REQUEST, "COMMENT4005", "올바른 사용자(댓글 작성자)가 아닙니다."),

    // 알림 관련 응답
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALARM4001", "알림이 없습니다")

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

