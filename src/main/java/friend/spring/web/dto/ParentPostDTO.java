package friend.spring.web.dto;

import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentPostDTO {
    Long postId;
    PostVoteType postVoteType;
    String nickname;
    String userImg;
    String title;
    String content;
    List<PollOptionDTO.PollOptionRes> pollOption;
    ParentPollDTO pollContent;
    Integer gauge;
    Integer like;
    Integer comment;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidatePostDTO {
        Long postId;
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
    public static class ParentPostGetListDTO {
        List<CandidatePostDTO> candidatePostDTOList;
        private Boolean isEnd;
    }
}


