package friend.spring.service;

import friend.spring.web.dto.PaymentResponseDTO;

public interface PaymentService {

    String previewOrderUid(Long orderId);

    PaymentResponseDTO previewOrderResponse(String orderUid);
}
