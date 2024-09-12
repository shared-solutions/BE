package friend.spring.repository;

import friend.spring.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<Order, Long> {
}
