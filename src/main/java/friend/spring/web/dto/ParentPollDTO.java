package friend.spring.web.dto;

import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentPollDTO{
    String candidateName;
    String candidateImage;
    Long candidateId;
    Integer rate;
    Long selection;
}
