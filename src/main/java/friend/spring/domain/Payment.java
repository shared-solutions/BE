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
    private BigDecimal amount;

    //주문 고유 번호
    private String merchantUid;

    //결제 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState = PaymentState.READY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 결제 상태 업데이트 편의 메서드
    public void updatePaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }


}
