package friend.spring.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ErrorReasonDTO {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final boolean isSuccess;
}
