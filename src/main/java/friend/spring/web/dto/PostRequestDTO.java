package friend.spring.web.dto;


import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.validation.annotation.ContentTextLimit;
import friend.spring.validation.annotation.DeadlineLimit;
import friend.spring.validation.annotation.TitleTextLimit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.jni.Poll;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PostRequestDTO {
    @Getter
    public static class AddPostDTO{
        @TitleTextLimit
        String title;
        @ContentTextLimit
        String content;
        String category;
        @NotNull
        Integer postType; // 1: vote, 2: review
        Integer postVoteType;// 1: general, 2: gauge
        String pollTitle;
        Boolean multipleChoice;
        Long parent_id;
        @DeadlineLimit
        LocalDateTime deadline;
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
        @TitleTextLimit
        String title;
        @ContentTextLimit
        String content;
        LocalDateTime deadline;
        Boolean voteOnGoing;
    }

}
