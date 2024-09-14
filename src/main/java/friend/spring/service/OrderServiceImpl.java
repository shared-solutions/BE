package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PaymentHandler;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.domain.Order;
import friend.spring.domain.Payment;
import friend.spring.domain.User;
import friend.spring.domain.enums.PaymentState;
import friend.spring.domain.enums.Product;
import friend.spring.repository.OrderRepository;
import friend.spring.repository.PaymentRepository;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.OrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;


    public Long createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request) {
        Product product = null;
        long price = 0L;

        if(orderRequestDTO.getItemName().equals("POINT_1000")) {
            product = Product.POINT_1000;
            price = 1000L;
        } else if (orderRequestDTO.getItemName().equals("POINT_2000")) {
            product = Product.POINT_2000;
            price = 2000L;
        }
        else {
            throw new PaymentHandler(ErrorStatus.ORDER_PRODUCT_NOT_FOUND);
        }

        Long userId = jwtTokenProvider.getCurrentUser(request);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        });

        Payment payment = Payment.builder()
                .price(BigDecimal.valueOf(price))
                .paymentState(PaymentState.ING)
                .build();

        paymentRepository.save(payment);

        Order order = Order.builder()
                .user(user)
                .price(BigDecimal.valueOf(price))
                .product(product)
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        Order result = orderRepository.save(order);

        return result.getId();
    }

    // 주문 체크 및 조회
    @Override
    public Order checkOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> {
            throw new PaymentHandler(ErrorStatus.ORDER_NOT_FOUND);
        });
    }

}
