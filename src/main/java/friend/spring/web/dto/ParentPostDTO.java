package friend.spring.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentPostDTO {
    String nickname;
    String title;
    String content;
    List<PollOptionDTO> pollOption;
    ParentPollDTO pollContent;
    Integer gauge;
    Integer like;
    Integer comment;
}


