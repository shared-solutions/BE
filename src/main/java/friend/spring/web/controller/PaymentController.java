package friend.spring.web.controller;

import com.siot.IamportRestClient.IamportClient;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.domain.Order;
import friend.spring.repository.OrderRepository;
import friend.spring.service.PaymentService;
import friend.spring.web.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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









}
