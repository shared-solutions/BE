package friend.spring.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import friend.spring.domain.Order;
import friend.spring.domain.Payment;
import friend.spring.repository.OrderRepository;
import friend.spring.web.dto.PaymentCallback;
import friend.spring.web.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final OrderRepository orderRepository;
    private final IamportClient iamportClient;

    @Override
    public String previewOrderUid(Long orderId) {
        Order result = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문 번호가 없습니다.")); // 추후 핸들러, 에러상태 수정
        return result.getOrderUid();
    }

    @Override
    public PaymentResponseDTO previewOrderResponse(String orderUid) {
        Order order = orderRepository.findOrderAndPaymentAndMember(orderUid).orElseThrow(() ->  new RuntimeException("주문이 존재하지 않습니다."));

        return PaymentResponseDTO.builder()
                .buyerName(order.getUser().getNickname())
                .buyerEmail(order.getUser().getEmail())
                .paymentPrice(order.getPayment().getPrice())
                .product(order.getProduct())
                .orderUid(order.getOrderUid())
                .build();
    }

    @Override
    public IamportResponse<Payment> paymentByCallBack(PaymentCallback paymentCallback) {
        try{
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = iam
        }
    }


}
