package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PostResponseDTO {

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
}

