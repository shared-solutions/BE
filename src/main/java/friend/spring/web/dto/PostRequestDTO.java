package friend.spring.web.dto;


import lombok.Getter;
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
//        Integer parent_id;
        List<String> pollOption;
        String file;
        String tag;
        Timestamp deadline;
        Integer point;
    }
}
