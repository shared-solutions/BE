package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import friend.spring.domain.enums.PaymentState;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    //결제 상태
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

    //주문 고유 번호
    private String paymentUid;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

}
