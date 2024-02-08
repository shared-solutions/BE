package friend.spring.web.dto;


import friend.spring.domain.Category;
import friend.spring.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Poll;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

public class PostRequestDTO {
    @Getter
    public static class AddPostDTO{
        @NotBlank
        String title;
        @NotBlank
        String content;
        String category;
        @NotBlank
        Integer postType; // 1: vote, 2: review
        Integer postVoteType;// 1: general, 2: gauge
        String pollTitle;
        Boolean multipleChoice;
        Long parent_id;
        @NotBlank
        Timestamp deadline;
        Integer point;
    }
    @Getter
    public static class ReviewPostGetDTO{
        Integer arrange; //조회순 :0, 최신순:1
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostEditReq {
        @NotBlank
        String title;
        @NotBlank
        String content;
    }

}
