package friend.spring.web.dto;

import friend.spring.domain.User;
import friend.spring.domain.enums.Gender;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

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

}