package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class commentCreateRes {
        Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class commentLikeRes {
        Long commentLikeId; // 댓글-좋아요 아이디
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class commentGetRes {
        Long commentId;
        String content;
        Long userId;
        String userNickname;
        String userImage;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        Long parentCommentId;
        Integer commentLike;
        List<commentGetRes> childrenComments;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class commentSelectRes {
        Long commentChoiceId;
        Integer point;
    }
}
