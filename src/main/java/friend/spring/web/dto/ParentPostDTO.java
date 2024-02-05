package friend.spring.web.dto;

import lombok.*;

import java.time.LocalDateTime;
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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidatePostDTO {
        String title;
        String content;
        Integer like;
        Integer comment;
        LocalDateTime createdAt;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentPostGetListDTO{
        List<CandidatePostDTO> candidatePostDTOList;
        private Boolean isEnd;
    }
}


