package friend.spring.web.dto;

import friend.spring.domain.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class UserResponseDTO {

//    public static List<TokenDTO> LoginResDTO;

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
    public static class PointViewDTO {
        Integer point;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthResponse {

        Boolean isLogin;
        TokenDTO accessToken;
        TokenDTO refreshToken;
        String email;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserSummaryInfo {

        Long user_id;
        String nickname;
        String image;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordUpdateRes {
        String newPassword;
    }
}
