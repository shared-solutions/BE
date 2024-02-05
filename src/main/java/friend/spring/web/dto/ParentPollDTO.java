package friend.spring.web.dto;

import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentPollDTO{
    Long candidateId;
    Integer rate;
    Long selection;
}
