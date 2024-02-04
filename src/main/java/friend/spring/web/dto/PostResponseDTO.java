package friend.spring.web.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddPostResultDTO {
        Long postId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostDTO {
        String nickName;
        LocalDateTime createdAt;
        String title;
        Integer postLike;
        Integer comment;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLikeRes {
        Long post_like_id;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSummaryListRes {
        String title;
        String content;
        Long post_id;
        String file;
        Integer like;
        Integer comment_cnt;
        LocalDateTime created_at;
        UserResponseDTO.UserSummaryInfo user;
        String postVoteType;
        Long general_poll_id;
        Long gauge_poll_id;
        Long card_poll_id;
        List<CandidateResponseDTO.CandidateSummaryRes> candidateList;
    }

}