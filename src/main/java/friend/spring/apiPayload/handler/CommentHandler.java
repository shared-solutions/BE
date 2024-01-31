package friend.spring.apiPayload.handler;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.BaseErrorCode;

public class CommentHandler extends GeneralException {
    public CommentHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
