package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SseResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentCreateResDTO {
        String userNickname;
        String userPhoto;
        String alarmType;
        String alarmContent;
        String commentContent;
        Long postId;
        Long commentId;
        LocalDateTime createdAt;
    }
}
