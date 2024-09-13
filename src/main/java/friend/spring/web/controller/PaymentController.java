package friend.spring.web.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.domain.Order;
import friend.spring.repository.OrderRepository;
import friend.spring.service.PaymentService;
import friend.spring.web.dto.PaymentCallback;
import friend.spring.web.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);  // 로그 출력을 위한 선언입니다.



    @GetMapping("/payment/{orderId}")
    public ApiResponse<PaymentResponseDTO> paymentPreview(@PathVariable Long orderId) {

        String orderUid = paymentService.previewOrderUid(orderId);
        PaymentResponseDTO paymentResponseDTO = paymentService.previewOrderResponse(orderUid);
        return ApiResponse.onSuccess(paymentResponseDTO);
    }

    @PostMapping("/payment")
    public ApiResponse<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallback paymentCallback) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallBack(paymentCallback);
        log.info("결제 응답입니다.", iamportResponse.getResponse().toString()); // 결제 응답 로그 출력입니다.
        return ApiResponse.onSuccess(iamportResponse);
    }


}
