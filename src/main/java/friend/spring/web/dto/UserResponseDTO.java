package friend.spring.web.dto;

import friend.spring.domain.User;
import friend.spring.domain.enums.Gender;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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

    public static class EmailDTO {
        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Email {
            Long id;
            String code;
            Boolean isCorrected;
            Boolean ExistEmail;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class UserJoinRes {
            private User user;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class UserInfo {
            private String email;
            private String nickname;
            private Gender gender;

        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailSendRes {
        private String code;
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