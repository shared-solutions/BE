package friend.spring.web.dto;

import friend.spring.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResDTO {
        String userPhoto;
        String userName;
        Integer userPoint;
        Integer userLevelInt;
        String userLevelName;
        Integer userRecommend;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResDTO {
        String userPhoto;
        String nickName;
        Integer recommend;
        String grade;
        Double nextGrade;
        String nextGradeName;
        Integer adoptComments;
        Double adoptCommentPercent;
        Integer postNum;
        Double adoptPostPercent;
        List<PostResponseDTO.MyPostDTO> postList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerResDTO {
        String userPhoto;
        String nickName;
        Integer recommend;
        String grade;
        Double nextGrade;
        String nextGradeName;
        Integer adoptComments;
        Double adoptCommentPercent;
        List<CommentResponseDTO.myCommentRes> commentList;
    }

}
