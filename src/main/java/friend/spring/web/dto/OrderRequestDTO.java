package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @Pattern(regexp = "^(POINT_1000|POINT_2000)$", message = "포인트는 POINT_1000 또는 POINT_2000 중 하나여야 합니다.")
    private String itemName;

}
