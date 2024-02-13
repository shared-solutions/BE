package friend.spring.kakao.converter;

import friend.spring.domain.User;
import friend.spring.domain.enums.RoleType;
import friend.spring.kakao.dto.response.KakaoProfile;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserResponseDTO;

public class AuthConverter {

    public static User kakaoUser(KakaoProfile kakaoProfile) {
        return User.builder()
                .nickname(kakaoProfile.getProperties().getNickname())
                .email(kakaoProfile.getKakao_account().getEmail())
                .role(RoleType.USER)
                .build();
    }

    public static UserResponseDTO.OAuthResponse toOAuthResponse(
            TokenDTO accessToken, TokenDTO refreshToken,Boolean isLogin, User user) {
        return UserResponseDTO.OAuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .isLogin(isLogin)
                .email(user.getEmail())
                .build();
    }

//    public static AuthResponseDTO.TokenRefreshResponse toTokenRefreshResponse(
//            TokenDTO accessToken, TokenDTO refreshToken) {
//        return AuthResponseDTO.TokenRefreshResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
}
