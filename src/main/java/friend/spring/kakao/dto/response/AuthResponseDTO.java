//package friend.spring.kakao.dto.response;
//
//import friend.spring.web.dto.TokenDTO;
//import lombok.*;
//
//public class AuthResponseDTO {
//
//    @Getter
//    @Builder
//    @AllArgsConstructor(access = AccessLevel.PROTECTED)
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class OAuthResponse {
//
//        Long userId;
//        TokenDTO accessToken;
//        TokenDTO refreshToken;
//        Boolean isLogin;
//    }
//
//    @Getter
//    @Builder
//    @AllArgsConstructor(access = AccessLevel.PROTECTED)
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class TokenRefreshResponse {
//
//        TokenDTO accessToken;
//        TokenDTO refreshToken;
//    }
//}