package friend.spring.service;

import friend.spring.domain.Order;
import friend.spring.web.dto.OrderRequestDTO;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    Long createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request);

    Order checkOrder(Long orderId);
}
