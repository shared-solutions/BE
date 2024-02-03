package friend.spring.web.dto;
import lombok.*;

import java.time.LocalDateTime;
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
}