package friend.spring.service;

import com.siot.IamportRestClient.response.IamportResponse;
import friend.spring.domain.Payment;
import friend.spring.web.dto.PaymentCallback;
import friend.spring.web.dto.PaymentResponseDTO;

public interface PaymentService {

    String previewOrderUid(Long orderId);

    PaymentResponseDTO previewOrderResponse(String orderUid);

    IamportResponse<Payment> paymentByCallBack(PaymentCallback paymentCallback);
}
