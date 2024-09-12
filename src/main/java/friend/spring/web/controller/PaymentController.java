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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/payment/{orderId}")
    public ApiResponse<PaymentResponseDTO> paymentPreview(@PathVariable Long orderId) {

        String orderUid = paymentService.previewOrderUid(orderId);
        PaymentResponseDTO paymentResponseDTO = paymentService.previewOrderResponse(orderUid);
        return ApiResponse.onSuccess(paymentResponseDTO);
    }

    @PostMapping("/payment")
    public IamportResponse<Payment> validationPayment(@RequestBody PaymentCallback paymentCallback) {
        IamportResponse<Payment> iamportResponse =
    }









}
