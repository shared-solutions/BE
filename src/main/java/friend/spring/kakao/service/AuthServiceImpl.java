package friend.spring.kakao.service;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.UserConverter;
import friend.spring.domain.User;
import friend.spring.kakao.dto.response.KakaoProfile;
import friend.spring.kakao.dto.response.OAuthToken;
import friend.spring.kakao.provider.KakaoAuthProvider;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import friend.spring.service.UserService;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserRequestDTO;
import friend.spring.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
//    private final RefreshTokenService refreshTokenService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public List<TokenDTO> kakaoLogin(String code) {
        OAuthToken oAuthToken = kakaoAuthProvider.requestToken(code);
        KakaoProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());

        System.out.println(kakaoProfile.getKakao_account().getEmail());
        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmail(
                        kakaoProfile.getKakao_account().getEmail());
        System.out.println(queryUser.get().getEmail());
        System.out.println(queryUser.get().getBirth());
//         가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
            TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            redisTemplate.opsForValue().set("RT:"+user.getEmail(),refreshToken.getToken(),refreshToken.getTokenExpriresTime().getTime(),TimeUnit.MILLISECONDS);

            List<TokenDTO> tokenDTOList = new ArrayList<>();
            tokenDTOList.add(refreshToken);
            tokenDTOList.add(accessToken);

            return tokenDTOList;
        } else {
            User user = userRepository.save(UserConverter.KakaoUser(kakaoProfile));
            TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
            TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            redisTemplate.opsForValue().set("RT:" + refreshToken, TimeUnit.MILLISECONDS);

            return (List<TokenDTO>) UserConverter.toOAuthResponse(accessToken, refreshToken, false, user);
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        if (queryUser.isPresent()) {
//            User user = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail())
//                    .orElseThrow(() -> new GeneralException(USERS_NOT_FOUND_EMAIL));//가입 안된 이메일
//            if (!encoder.matches(kakaoProfile.getKakao_account().getEmail(), user.getPassword())) {
//                throw new GeneralException(PASSWORD_INCORRECT); //비밀번호 불일치, 계정없음
//            }
//            //토큰발급
//            TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
//            TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
//
//            // login 시 Redis에 RT:gominchingy@gmail.com(key): --refresh token실제값--(value) 형태로 refresh 토큰 저장하기
//            // opsForValue() : set을 통해 key,value값 저장하고 get(key)통해 value가져올 수 있음.
//            // refreshToken.getTokenExpriresTime().getTime() : 리프레시 토큰의 만료시간이 지나면 해당 값 자동 삭제
//            redisTemplate.opsForValue().set("RT:" + user.getEmail(), refreshToken.getToken(), refreshToken.getTokenExpriresTime().getTime(), TimeUnit.MILLISECONDS);
//
//            List<TokenDTO> tokenDTOList = new ArrayList<>();
//            tokenDTOList.add(accessToken);
//            tokenDTOList.add(refreshToken);
//            System.out.println(tokenDTOList);
//
//            return tokenDTOList;
//        }
//        return UserResponseDTO.OAuthResponse();
        }
    }
}
//    @Override
//    @Transactional
//    public JwtTokenProvider refresh(String refreshToken) {
//        jwtTokenProvider.validateToken(refreshToken);
//
//        if (!jwtTokenProvider.validateToken(refreshToken)) {
//            throw new GeneralException(ErrorStatus.NOT_EQUAL_TOKEN);
//        }
//
//        TokenDTO accessToken =
//                jwtTokenProvider.createAccessToken(jwtTokenProvider.getemail(refreshToken));
//        TokenDTO newRefreshToken =
//                jwtTokenProvider.createAccessToken(jwtTokenProvider.getemail(refreshToken));
////        refreshTokenService.saveToken(newRefreshToken);
//        return AuthConverter.toTokenRefreshResponse(accessToken, refreshToken);
//    }

//    public void logout(Long userId) {
////        refreshTokenService.deleteToken(userId);
////    }
//}