package friend.spring.apiPayload.handler;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.BaseErrorCode;

public class PostHandler extends GeneralException {
    public PostHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
