package friend.spring.apiPayload.handler;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.BaseErrorCode;

public class PaymentHandler extends GeneralException {
    public PaymentHandler(BaseErrorCode code) {
        super(code);
    }
}
