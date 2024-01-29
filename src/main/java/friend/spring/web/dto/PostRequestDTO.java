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
        @NotBlank
        Integer postType; // 1: not vote, 2: vote, 3: review
        Integer category; //미정
        Integer postVoteType;// 1: general, 2: gauge
        Long parent_id;
        List<PollOptionDTO> pollOption;
        List<String> tag;
        Timestamp deadline;
        Integer point;
    }


}
