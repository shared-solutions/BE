package friend.spring.web.dto;


import friend.spring.domain.Post;
import lombok.Getter;
import org.apache.tomcat.jni.Poll;

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
        Integer category; //미정
        @NotBlank
        Integer postType; // 1: vote, 2: review
        Integer postVoteType;// 1: general, 2: gauge
        String pollTitle;
        Boolean multipleChoice;
        List<PollOptionDTO> pollOption;
        Long parent_id;
        @NotBlank
        Timestamp deadline;
        Integer point;
    }


}
