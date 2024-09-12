package friend.spring.web.dto;

import friend.spring.domain.enums.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentResponseDTO {
        private String orderUid;
        private Product product;
        private String buyerName;
        private String buyerEmail;
        private BigDecimal paymentPrice;

}
