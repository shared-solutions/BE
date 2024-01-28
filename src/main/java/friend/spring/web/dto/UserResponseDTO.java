package friend.spring.web.dto;

import lombok.*;

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
    }
}