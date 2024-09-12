package friend.spring.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import friend.spring.domain.Order;

import friend.spring.repository.OrderRepository;
import friend.spring.repository.PaymentRepository;
import friend.spring.web.dto.PaymentCallback;
import friend.spring.web.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
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
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(paymentCallback.getPaymentUid());
            // 주문 내역 조회
            Order order = orderRepository.findOrderAndPayment(paymentCallback.getOrderUid()).orElseThrow(() -> new RuntimeException("주문이 없습니다."));

            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());
                throw new RuntimeException("결제 미완료 에러입니다.");
            }

            // 데이터 베이스상에 있는 결제금액
            BigDecimal price = order.getPayment().getPrice();
            // 실제 결제금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if (iamportPrice != price) {
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                // 결제금액 위변조로 의심되는 결제 금액을 취소
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }
            // 결제 상태 변경
            // order.getPayment().


        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
