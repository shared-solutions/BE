package friend.spring.apiPayload.handler;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
