package friend.spring.web.dto;

import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollOptionDTO {
    private String optionString;
    private String optionImg;
}
