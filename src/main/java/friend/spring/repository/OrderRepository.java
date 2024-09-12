package friend.spring.repository;

import friend.spring.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o " + "LEFT JOIN FETCH o.payment p " + "LEFT JOIN FETCH o.member m " + "WHERE o.orderUid = :orderUid")
    Optional<Order> findOrderAndPaymentAndMember(@Param("orderUid") String orderUid);
}
}
