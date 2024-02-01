package friend.spring.web.dto;
import friend.spring.domain.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailResponse {
        String nickname;
        LocalDateTime createdAt;
        String title;
        String content;
        String pollTitle; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO> pollOption; // 투표글에서만 사용, 후기글에서는 null
        Integer gauge; // 게이지 투표글에서만 사용, 후기글에서는 null
        Integer point; // 투표글에서만 사용, 후기글에서는 null
        Long parentId; // 후기글에서만 사용, 일반글에서는 null
        Timestamp deadline; // 투표글에서만 사용, 후기글에서는 null
        Integer view;
        Integer like;
        Integer comment;

    }
}