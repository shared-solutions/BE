package friend.spring.apiPayload.handler;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.BaseErrorCode;

public class AlarmHandler extends GeneralException {
    public AlarmHandler(BaseErrorCode errorCode){super(errorCode);}
}
