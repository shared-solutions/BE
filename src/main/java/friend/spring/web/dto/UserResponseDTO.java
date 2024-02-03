package friend.spring.web.dto;

import friend.spring.domain.User;
import friend.spring.domain.enums.Gender;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class JoinResultDTO {

        String email;
        LocalDate createAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserInfo {
        private String email;
        private String nickname;
        private Gender gender;

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
  
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDTO {

        String type;
        String accessToken;
        String refreshToken;
        String tokenExpiresTime;


    }

//    @Getter
//    @Builder
//    @AllArgsConstructor(access = AccessLevel.PROTECTED)
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class TokenRefreshResponse {
//
//        String accessToken;
//        String refreshToken;
//    }

}