package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.domain.Order;
import friend.spring.service.OrderService;
import friend.spring.web.dto.OrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;


    // 주문 번호 생성 api
    @PostMapping("/request")
    public ApiResponse<Long> createOrder(@RequestBody OrderRequestDTO orderRequestDTO, @RequestHeader("atk") String atk, HttpServletRequest request) {
        Long orderId = orderService.createOrder(orderRequestDTO, request);
        return ApiResponse.onSuccess(orderId);
    }

    // 생성된 주문 id 로 체크
    @GetMapping("/check")
    public Order checkOrder(Long orderId, @RequestHeader("atk") String atk, HttpServletRequest request) {
        return orderService.checkOrder(orderId);
    }
}
