package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

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
        Boolean isDeleted; // 댓글 삭제 여부
        Boolean isMyComment; // 내가 쓴 댓글인지 여부
        Boolean isPushedLike; // 좋아요 이미 눌렀는지 여부
        Boolean isOwnerOfPost; // 내가 쓴 글인지 여부
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
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myCommentRes {
        String nickName;
        LocalDateTime createdAt;
        String content;
        Integer commentLike;
        Integer reComment;
    }
}
